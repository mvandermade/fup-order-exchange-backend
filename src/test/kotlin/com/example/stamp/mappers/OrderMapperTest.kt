package com.example.stamp.mappers

import com.example.stamp.entities.Order
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderMapperTest {
    private val orderMapper = OrderMapper()

    @Test
    fun `Map response`() {
        val order = minRandom<Order>()
        val response = orderMapper.toResponse(order)

        assertThat(response.orderId).isEqualTo(order.id)
        assertThat(response.createdAt).isEqualTo(order.createdAt)
    }
}
