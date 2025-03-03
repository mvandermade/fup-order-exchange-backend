package com.example.stamp.controllers

import com.example.stamp.controllers.requests.StampCodeReportPutV1Request
import com.example.stamp.controllers.requests.StampCodeReportV1Request
import com.example.stamp.controllers.responses.StampCodeReportV1Response
import com.example.stamp.entities.StampReportEntity
import com.example.stamp.repositories.StampReportRepository
import com.example.stamp.testutils.buildPostgresContainer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.OffsetDateTime

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ReportsV1ControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val stampReportRepository: StampReportRepository,
) {
    @Test
    fun `POST should persist report in database but not confirm it`() {
        val offsetDateTime = OffsetDateTime.now()
        val stampCodeRequest =
            StampCodeReportV1Request(
                code = "ABCD",
                offsetDateTime = offsetDateTime,
                reachedDestination = true,
                comment = "HEY HELLO FROM THE OBSERVER",
            )

        val response =
            mockMvc.perform(
                post("$PATH/stamp-code")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(stampCodeRequest)),
            )
                .andExpect(status().isAccepted)
                .andReturn().let { objectMapper.readValue<StampCodeReportV1Response>(it.response.contentAsString) }

        //  Check if it is actually in the DB
        val reportInDB =
            stampReportRepository.findByIdOrNull(response.id)
                ?: throw NullPointerException("reportNotInDB")

        assertThat(response.id).isEqualTo(reportInDB.id)
        assertThat(response.reportIsConfirmed).isFalse()
    }

    @Nested
    inner class PutStampCode {
        @Test
        fun `Put order in database and be able to confirm it`() {
            val reportEntity =
                stampReportRepository.save(
                    minRandom<StampReportEntity>().apply {
                        reportIsConfirmed = false
                    },
                )

            val orderRequest = StampCodeReportPutV1Request(reportIsConfirmed = true)

            mockMvc.perform(
                put("$PATH/stamp-code/${reportEntity.id}")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(orderRequest)),
            ).andExpect(status().isOk)

            val reportInDb =
                stampReportRepository.findByIdOrNull(reportEntity.id)
                    ?: throw NullPointerException("report ${reportEntity.id} not found")

            assertThat(reportInDb.reportIsConfirmed).isTrue()
        }

        @Test
        fun `Expect exception not found`() {
            val reportRequest = StampCodeReportPutV1Request(reportIsConfirmed = true)

            val result =
                mockMvc.perform(
                    put("$PATH/stamp-code/0")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reportRequest)),
                ).andExpect(status().is4xxClientError).andReturn()

            JSONAssert.assertEquals(
                """
                {
                    "httpStatus": 404,
                    "message": "Stamp code report not found",
                    "origin": "reportId",
                    "originId": "0",
                    "errorCode": "STAMP_CODE_REPORT_NOT_FOUND",
                    "service": "made-fp"
                }
                """.trimIndent(),
                result.response.contentAsString,
                true,
            )
        }

        @Test
        fun `Expect exception already confirmed`() {
            val reportEntity =
                stampReportRepository.save(
                    minRandom<StampReportEntity>().apply {
                        reportIsConfirmed = true
                    },
                )

            val reportRequest = StampCodeReportPutV1Request(reportIsConfirmed = true)

            val result =
                mockMvc.perform(
                    put("$PATH/stamp-code/${reportEntity.id}")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reportRequest)),
                ).andExpect(status().is4xxClientError).andReturn()

            JSONAssert.assertEquals(
                """
                {
                    "httpStatus": 406,
                    "message": "Stamp report is confirmed, no modifications possible anymore.",
                    "origin": "reportId",
                    "originId": "${reportEntity.id}",
                    "errorCode":"STAMP_IS_CONFIRMED",
                    "service": "made-fp"
                }
                """.trimIndent(),
                result.response.contentAsString,
                true,
            )
        }
    }

    companion object {
        const val PATH = "/v1/reports"

        @Container
        @ServiceConnection
        val postgresContainer = buildPostgresContainer()
    }
}
