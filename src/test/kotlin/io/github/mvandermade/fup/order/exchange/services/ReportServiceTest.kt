package io.github.mvandermade.fup.order.exchange.services

import com.ninjasquad.springmockk.MockkBean
import io.github.mvandermade.fup.order.exchange.controllers.requests.StampReportV1Request
import io.github.mvandermade.fup.order.exchange.providers.TimeProvider
import io.github.mvandermade.fup.order.exchange.repositories.StampReportIdempotencyKeyRepository
import io.github.mvandermade.fup.order.exchange.repositories.StampReportRepository
import io.github.mvandermade.fup.order.exchange.testutils.buildPostgresContainer
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
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
    @param:Autowired private val reportService: ReportService,
    @param:Autowired private val stampReportRepository: StampReportRepository,
    @param:Autowired private val stampReportIdempotencyKeyRepository: StampReportIdempotencyKeyRepository,
) {
    @MockkBean
    private lateinit var timeProvider: TimeProvider

    @BeforeEach
    fun setUp() {
        stampReportIdempotencyKeyRepository.deleteAll()
        stampReportRepository.deleteAll()
    }

    @Test
    fun `Correct timestamp is set when creating a report and flipped to true`() {
        // The GHA server has a higher precision than the database can save so truncate it
        val offsetDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MICROS)

        every { timeProvider.offsetDateTime() } returns offsetDateTime

        val dto =
            reportService.postStampReport(
                StampReportV1Request(
                    code = "123",
                    offsetDateTime = offsetDateTime,
                    reachedDestination = true,
                    comment = "my comment",
                ),
                "abc",
            )

        val report =
            stampReportRepository.findByIdOrNull(dto.id)
                ?: throw NullPointerException("Report with id ${dto.id} not found")

        assertThat(report.code).isEqualTo("123")
        assertThat(report.reachedDestination).isTrue
        assertThat(report.comment).isEqualTo("my comment")
        assertThat(report.createdAtObserver).isEqualTo(offsetDateTime)
    }

    @Test
    fun `Idempotency key is saved`() {
        reportService.postStampReport(
            StampReportV1Request(
                code = "123",
                offsetDateTime = null,
                reachedDestination = null,
                comment = null,
            ),
            "abc",
        )

        assertThat(stampReportIdempotencyKeyRepository.findByUserKey("abc")).isNotNull
    }

    companion object {
        @Container
        @ServiceConnection
        val postgresContainer = buildPostgresContainer()
    }
}
