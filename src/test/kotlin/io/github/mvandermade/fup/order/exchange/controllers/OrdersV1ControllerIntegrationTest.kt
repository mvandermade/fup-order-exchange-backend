package io.github.mvandermade.fup.order.exchange.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.mvandermade.fup.order.exchange.controllers.responses.OrderV1Response
import io.github.mvandermade.fup.order.exchange.entities.OrderEntity
import io.github.mvandermade.fup.order.exchange.entities.StampEntity
import io.github.mvandermade.fup.order.exchange.repositories.OrderRepository
import io.github.mvandermade.fup.order.exchange.repositories.OrderStampRepository
import io.github.mvandermade.fup.order.exchange.repositories.StampRepository
import io.github.mvandermade.fup.order.exchange.testutils.buildPostgresContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrdersV1ControllerIntegrationTest(
    @param:Autowired private val orderRepository: OrderRepository,
    @param:Autowired private val objectMapper: ObjectMapper,
    @param:Autowired private val mockMvc: MockMvc,
    @param:Autowired private val stampRepository: StampRepository,
    @param:Autowired private val orderStampRepository: OrderStampRepository,
) {
    @BeforeEach
    fun setUp() {
        orderStampRepository.deleteAll()
        stampRepository.deleteAll()
        orderRepository.deleteAll()
    }

    @Test
    fun `POST should persist order in database`() {
        val response =
            mockMvc
                .perform(post(PATH).header("idempotency-key", "temp"))
                .andExpect(status().isOk)
                .andReturn()
                .let { objectMapper.readValue<OrderV1Response>(it.response.contentAsString) }

        //  Check if it is actually in the DB
        val orderInDB =
            orderRepository.findByIdOrNull(response.id)
                ?: throw NullPointerException("orderInDB")

        assertThat(response.id).isEqualTo(orderInDB.id)
        assertThat(response.stamp).isEqualTo(null)
    }

    @Nested
    inner class GetOrder {
        @Test
        fun `Should get a stamp after waiting a bit`() {
            val orderEntity = orderRepository.save(OrderEntity())
            stampRepository.save(
                StampEntity().apply {
                    this.code = "ABCD"
                },
            )

            val result =
                mockMvc
                    .perform(get("$PATH/${orderEntity.id}"))
                    .andExpect(status().isOk)
                    .andReturn()
                    .let { objectMapper.readValue<OrderV1Response>(it.response.contentAsString) }

            assertThat(result.stamp?.code).isEqualTo("ABCD")
        }

        @Test
        fun `Expect order not found`() {
            val result =
                mockMvc
                    .perform(get("$PATH/0"))
                    .andExpect(status().is4xxClientError)
                    .andReturn()

            JSONAssert.assertEquals(
                """
                {
                    "httpStatus": 404,
                    "message": "Order not found",
                    "origin": "orderId",
                    "originId": "0",
                    "errorCode": "ORDER_NOT_FOUND",
                    "service": "made-fp"
                }
                """.trimIndent(),
                result.response.contentAsString,
                true,
            )
        }
    }

    companion object {
        const val PATH = "/v1/orders"

        @Container
        @ServiceConnection
        @Suppress("unused")
        val postgresContainer = buildPostgresContainer()
    }
}
