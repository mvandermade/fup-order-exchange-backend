package com.example.stamp.controllers

import com.example.stamp.annotations.SpringBootTestWithCleanup
import com.example.stamp.controllers.requests.StampCodeReportV1Request
import com.example.stamp.controllers.responses.StampCodeReportV1Response
import com.example.stamp.repositories.StampReportRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.OffsetDateTime

@SpringBootTestWithCleanup
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

    companion object {
        const val PATH = "/v1/reports"
    }
}
