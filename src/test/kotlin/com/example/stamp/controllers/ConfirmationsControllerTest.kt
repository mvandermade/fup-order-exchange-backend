package com.example.stamp.controllers

import com.example.stamp.annotations.SpringBootTestWithCleanup
import com.example.stamp.controllers.requests.OrderConfirmRequest
import com.example.stamp.entities.OrderEntity
import com.example.stamp.repositories.OrderRepository
import com.fasterxml.jackson.databind.ObjectMapper
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTestWithCleanup
@AutoConfigureMockMvc
class ConfirmationsControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val orderRepository: OrderRepository,
) {
    @Test
    fun `Put order in database and be able to confirm it`() {
        val orderEntity =
            orderRepository.save(
                minRandom<OrderEntity>().apply {
                    orderConfirmed = false
                },
            )

        val request = OrderConfirmRequest(orderId = orderEntity.id)

        mockMvc.perform(
            put(PATH)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        ).andExpect(status().isOk)

        val orderInDB =
            orderRepository.findByIdOrNull(orderEntity.id)
                ?: throw NullPointerException("order ${orderEntity.id} not found")

        assertThat(orderInDB.orderConfirmed).isTrue()
    }

    @Test
    fun `Expect exception`() {
        val request = OrderConfirmRequest(orderId = 0)
        val result =
            mockMvc.perform(
                put(PATH)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().is4xxClientError).andReturn()

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
        const val PATH = "/v1/confirmations"
    }
}
