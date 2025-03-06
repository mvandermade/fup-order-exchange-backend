package com.example.stamp.services

import com.example.stamp.controllers.requests.StampCodeReportPutV1Request
import com.example.stamp.entities.StampReportEntity
import com.example.stamp.providers.TimeProvider
import com.example.stamp.repositories.StampReportRepository
import com.example.stamp.testutils.buildPostgresContainer
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@SpringBootTest
@Testcontainers
class ReportServiceTest(
    @Autowired private val reportService: ReportService,
    @Autowired private val stampReportRepository: StampReportRepository,
) {
    @MockkBean
    private lateinit var timeProvider: TimeProvider

    @Test
    fun `Correct timestamp is set when creating a report and flipped to true`() {
        val entity =
            stampReportRepository.save(
                minRandom<StampReportEntity>().apply {
                    reportIsConfirmed = false
                },
            )

        // The GHA server has a higher precision than the database can save so truncate it
        val offsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MICROS)

        every { timeProvider.offsetDateTime() } returns offsetDateTime

        reportService.putReport(entity.id, StampCodeReportPutV1Request(true))

        val report =
            stampReportRepository.findByIdOrNull(entity.id)
                ?: throw NullPointerException("Report with id ${entity.id} not found")

        verify(exactly = 1) { timeProvider.offsetDateTime() }
        assertThat(report.reportIsConfirmed).isTrue()

        assertThat(report.reportIsConfirmedAt).isEqualTo(offsetDateTime)
    }

    companion object {
        @Container
        @ServiceConnection
        val postgresContainer = buildPostgresContainer()
    }
}
