package com.example.stamp.controllers

import com.example.stamp.annotations.SpringBootTestWithCleanup
import com.example.stamp.controllers.responses.StampResponse
import com.example.stamp.entities.Order
import com.example.stamp.entities.Stamp
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.StampRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTestWithCleanup
@AutoConfigureMockMvc
class StampControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val stampRepository: StampRepository,
) {
    @Test
    fun `Should get a stamp after waiting a bit`() {
        val order = orderRepository.save(Order().apply { orderConfirmed = true })
        stampRepository.save(
            Stamp().apply {
                this.code = "ABCD"
            },
        )

        val result =
            mockMvc.perform(get("$PATH/collect/${order.id}"))
                .andExpect(status().isOk)
                .andReturn().let { objectMapper.readValue<StampResponse>(it.response.contentAsString) }

        assertThat(result.code).isEqualTo("ABCD")
    }

    @Test
    fun `Expect order not found`() {
        val result =
            mockMvc.perform(get("$PATH/collect/0"))
                .andExpect(status().is4xxClientError)
                .andReturn()

        JSONAssert.assertEquals(
            """
            {
                "httpStatus": 404,
                "message": "Order not found",
                "origin": "orderId",
                "originId": "0"
            }
            """.trimIndent(),
            result.response.contentAsString,
            true,
        )
    }

    companion object {
        const val PATH = "/v1/stamps"
    }
}
