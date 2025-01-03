package com.example.stamp.controllers

import com.example.stamp.annotations.SpringBootTestWithCleanup
import com.example.stamp.controllers.requests.OrderV1Request
import com.example.stamp.controllers.responses.OrderV1Response
import com.example.stamp.entities.OrderEntity
import com.example.stamp.entities.StampEntity
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.StampRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTestWithCleanup
@AutoConfigureMockMvc
class OrdersControllerV1Test(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val stampRepository: StampRepository,
) {
    @Test
    fun `POST should persist order in database but not confirm it`() {
        val response =
            mockMvc.perform(post(PATH))
                .andExpect(status().isAccepted)
                .andReturn().let { objectMapper.readValue<OrderV1Response>(it.response.contentAsString) }

        //  Check if it is actually in the DB
        val orderInDB =
            orderRepository.findByIdOrNull(response.id)
                ?: throw NullPointerException("orderInDB")

        assertThat(response.id).isEqualTo(orderInDB.id)
        assertThat(response.orderIsConfirmed).isFalse()
    }

    @Nested
    inner class PutOrder {
        @Test
        fun `Put order in database and be able to confirm it`() {
            val orderEntity =
                orderRepository.save(
                    minRandom<OrderEntity>().apply {
                        orderIsConfirmed = false
                    },
                )

            val orderRequest = OrderV1Request(orderIsConfirmed = true)

            mockMvc.perform(
                put("$PATH/${orderEntity.id}")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(orderRequest)),
            ).andExpect(status().isOk)

            val orderInDB =
                orderRepository.findByIdOrNull(orderEntity.id)
                    ?: throw NullPointerException("order ${orderEntity.id} not found")

            assertThat(orderInDB.orderIsConfirmed).isTrue()
        }

        @Test
        fun `Expect exception not found`() {
            val orderRequest = OrderV1Request(orderIsConfirmed = true)

            val result =
                mockMvc.perform(
                    put("$PATH/0")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderRequest)),
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

        @Nested
        inner class PutOrder {
            @Test
            fun `Put order in database and be able to confirm it`() {
                val orderEntity =
                    orderRepository.save(
                        minRandom<OrderEntity>().apply {
                            orderIsConfirmed = false
                        },
                    )

                val orderRequest = OrderV1Request(orderIsConfirmed = true)

                mockMvc.perform(
                    put("$PATH/${orderEntity.id}")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderRequest)),
                ).andExpect(status().isOk)

                val orderInDB =
                    orderRepository.findByIdOrNull(orderEntity.id)
                        ?: throw NullPointerException("order ${orderEntity.id} not found")

                assertThat(orderInDB.orderIsConfirmed).isTrue()
            }

            @Test
            fun `Expect exception already reported`() {
                val orderEntity =
                    orderRepository.save(
                        minRandom<OrderEntity>().apply {
                            orderIsConfirmed = true
                        },
                    )

                val orderRequest = OrderV1Request(orderIsConfirmed = true)

                val result =
                    mockMvc.perform(
                        put("$PATH/${orderEntity.id}")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(orderRequest)),
                    ).andExpect(status().is4xxClientError).andReturn()

                JSONAssert.assertEquals(
                    """
                    {
                        "httpStatus": 406,
                        "message": "Order is confirmed, no modifications possible anymore.",
                        "origin": "orderId",
                        "originId": "${orderEntity.id}"
                    }
                    """.trimIndent(),
                    result.response.contentAsString,
                    true,
                )
            }
        }
    }

    @Nested
    inner class GetOrder {
        @Test
        fun `Should get a stamp after waiting a bit`() {
            val orderEntity = orderRepository.save(OrderEntity().apply { orderIsConfirmed = true })
            stampRepository.save(
                StampEntity().apply {
                    this.code = "ABCD"
                },
            )

            val result =
                mockMvc.perform(get("$PATH/${orderEntity.id}"))
                    .andExpect(status().isOk)
                    .andReturn().let { objectMapper.readValue<OrderV1Response>(it.response.contentAsString) }

            assertThat(result.stamp?.code).isEqualTo("ABCD")
        }

        @Test
        fun `Expect order not found`() {
            val result =
                mockMvc.perform(get("$PATH/0"))
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
    }

    companion object {
        const val PATH = "/v1/orders"
    }
}
