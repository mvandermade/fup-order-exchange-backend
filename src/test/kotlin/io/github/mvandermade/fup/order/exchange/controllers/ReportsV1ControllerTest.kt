package io.github.mvandermade.fup.order.exchange.controllers

import com.example.stamp.controllers.requests.StampReportV1Request
import com.example.stamp.controllers.responses.StampReportV1Response
import com.example.stamp.repositories.StampReportIdempotencyKeyRepository
import com.example.stamp.repositories.StampReportRepository
import com.example.stamp.services.ReportService
import com.example.stamp.testutils.buildPostgresContainer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.OffsetDateTime

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ReportsV1ControllerTest(
    @param:Autowired private val mockMvc: MockMvc,
    @param:Autowired private val objectMapper: ObjectMapper,
    @param:Autowired private val stampReportRepository: StampReportRepository,
    @param:Autowired private val stampReportIdempotencyKeyRepository: StampReportIdempotencyKeyRepository,
) {
    @SpykBean
    private lateinit var reportService: ReportService

    @BeforeEach
    fun setUp() {
        stampReportIdempotencyKeyRepository.deleteAll()
        stampReportRepository.deleteAll()
    }

    @Test
    fun `POST should persist report in database`() {
        val offsetDateTime = OffsetDateTime.now()
        val stampRequest =
            StampReportV1Request(
                code = "ABCD",
                offsetDateTime = offsetDateTime,
                reachedDestination = true,
                comment = "HEY HELLO FROM THE OBSERVER",
            )

        val response =
            mockMvc
                .perform(
                    post("$PATH/stamp-code")
                        .header("idempotency-key", "abc")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(stampRequest)),
                ).andExpect(status().isOk)
                .andReturn()
                .let { objectMapper.readValue<StampReportV1Response>(it.response.contentAsString) }

        //  Check if it is actually in the DB
        val reportInDB =
            stampReportRepository.findByIdOrNull(response.id)
                ?: throw NullPointerException("reportNotInDB")

        assertThat(response.id).isEqualTo(reportInDB.id)
    }

    @Test
    fun `Idempotency key is saved and cached`() {
        val stampReportRequest =
            StampReportV1Request(
                code = "ABCD",
                offsetDateTime = null,
                reachedDestination = null,
                comment = null,
            )

        mockMvc
            .perform(
                post("$PATH/stamp-code")
                    .header("idempotency-key", "idpk")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(stampReportRequest)),
            ).andExpect(status().isOk)
            .andReturn()
            .let { objectMapper.readValue<StampReportV1Response>(it.response.contentAsString) }

        assertThat(stampReportIdempotencyKeyRepository.findByUserKey("idpk")).isNotNull
        verify(exactly = 1) { reportService.postStampReport(any(), "idpk") }
        verify(exactly = 0) { reportService.getStampReport(any()) }

        // Check the cache works

        mockMvc
            .perform(
                post("$PATH/stamp-code")
                    .header("idempotency-key", "idpk")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(stampReportRequest)),
            ).andExpect(status().isOk)

        verify(exactly = 1) { reportService.getStampReport(any()) }
    }

    companion object {
        const val PATH = "/v1/reports"

        @Container
        @ServiceConnection
        @Suppress("unused")
        val postgresContainer = buildPostgresContainer()
    }
}
