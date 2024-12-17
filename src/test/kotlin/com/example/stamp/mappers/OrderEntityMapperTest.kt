package com.example.stamp.mappers

import com.example.stamp.entities.OrderEntity
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderEntityMapperTest {
    private val orderMapper = OrderMapper()

    @Test
    fun `Map response`() {
        val orderEntity = minRandom<OrderEntity>()
        val response = orderMapper.toResponse(orderEntity)

        assertThat(response.orderId).isEqualTo(orderEntity.id)
        assertThat(response.createdAt).isEqualTo(orderEntity.createdAt)
    }
}
