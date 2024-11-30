package com.example.stamp.mapper

import com.example.stamp.entity.Order
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderMapperTest {
    val orderMapper = OrderMapper()

    @Test
    fun `Map response`() {
        val order = minRandom<Order>()
        val response = orderMapper.toResponse(order)

        assertThat(response.orderId).isEqualTo(order.id)
        assertThat(response.createdAt).isEqualTo(order.createdAt)
    }
}
