package com.example.stamp.services

import com.example.stamp.annotations.SpringBootTestWithCleanup
import com.example.stamp.controllers.requests.StampCodeReportPutV1Request
import com.example.stamp.entities.StampReportEntity
import com.example.stamp.providers.TimeProvider
import com.example.stamp.repositories.StampReportRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.time.OffsetDateTime

@SpringBootTestWithCleanup
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

        val offsetDateTime = OffsetDateTime.now()

        every { timeProvider.offsetDateTime() } returns offsetDateTime

        reportService.putReport(entity.id, StampCodeReportPutV1Request(true))

        val report =
            stampReportRepository.findByIdOrNull(entity.id)
                ?: throw NullPointerException("Report with id ${entity.id} not found")

        verify { timeProvider.offsetDateTime() }
        assertThat(report.reportIsConfirmed).isEqualTo(true)
        assertThat(report.reportIsConfirmedAt?.dayOfMonth).isEqualTo(offsetDateTime.dayOfMonth)
        assertThat(report.reportIsConfirmedAt?.dayOfWeek).isEqualTo(offsetDateTime.dayOfWeek)
        assertThat(report.reportIsConfirmedAt?.hour).isEqualTo(offsetDateTime.hour)
        assertThat(report.reportIsConfirmedAt?.minute).isEqualTo(offsetDateTime.minute)
    }
}
