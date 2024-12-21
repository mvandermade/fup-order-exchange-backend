package com.example.stamp.controllers

import com.example.stamp.annotations.SpringBootTestWithCleanup
import com.example.stamp.controllers.responses.StampV1Response
import com.example.stamp.entities.OrderEntity
import com.example.stamp.entities.StampEntity
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
class StampsControllerV1Test(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val stampRepository: StampRepository,
) {
    @Test
    fun `Should get a stamp after waiting a bit`() {
        val orderEntity = orderRepository.save(OrderEntity().apply { orderIsConfirmed = true })
        stampRepository.save(
            StampEntity().apply {
                this.code = "ABCD"
            },
        )

        val result =
            mockMvc.perform(get("$PATH/collect/${orderEntity.id}"))
                .andExpect(status().isOk)
                .andReturn().let { objectMapper.readValue<StampV1Response>(it.response.contentAsString) }

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
