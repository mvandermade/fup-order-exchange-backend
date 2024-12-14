package com.example.stamp.controllers

import com.example.stamp.controllers.responses.OrderResponse
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val mockMvc: MockMvc,
) {
    @BeforeEach
    fun setUp(
        @Autowired orderRepository: OrderRepository,
        @Autowired orderStampRepository: OrderStampRepository,
        @Autowired stampRepository: StampRepository,
    ) {
        orderStampRepository.deleteAllInBatch()
        stampRepository.deleteAllInBatch()
        orderRepository.deleteAllInBatch()
    }

    @Test
    fun `Fetching should persist order in database`() {
        val response =
            mockMvc.perform(post(PATH))
                .andExpect(status().isOk)
                .andReturn().let { objectMapper.readValue<OrderResponse>(it.response.contentAsString) }

        //  Check if it is actually in the DB
        val orderInDB =
            orderRepository.findByIdOrNull(response.orderId)
                ?: throw NullPointerException("orderInDB")

        assertThat(response.createdAt).isEqualTo(orderInDB.createdAt)
    }

    companion object {
        const val PATH = "/v1/orders"
    }
}
