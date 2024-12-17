package com.example.stamp.mappers

import com.example.stamp.entities.OrderEntity
import com.example.stamp.models.Order
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderEntityMapperTest {
    private val orderMapper = OrderMapper()

    @Test
    fun `Map order to response`() {
        val order = minRandom<Order>()
        val response = orderMapper.toResponse(order)

        assertThat(response.orderId).isEqualTo(order.id)
        assertThat(response.createdAt).isEqualTo(order.createdAt)
    }

    @Test
    fun `Map entity to order`() {
        val orderEntity = minRandom<OrderEntity>()
        val order = orderMapper.toOrder(orderEntity)

        assertThat(order.id).isEqualTo(orderEntity.id)
        assertThat(order.createdAt).isEqualTo(orderEntity.createdAt)
    }
}
