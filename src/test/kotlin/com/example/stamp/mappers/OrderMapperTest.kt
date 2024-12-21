package com.example.stamp.mappers

import com.example.stamp.datatransferobjects.OrderDTO
import com.example.stamp.entities.OrderEntity
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderMapperTest {
    private val orderMapper = OrderMapper()

    @Test
    fun `Map order to response`() {
        val orderDTO = minRandom<OrderDTO>()
        val response = orderMapper.toResponse(orderDTO)

        assertThat(response.orderId).isEqualTo(orderDTO.id)
        assertThat(response.orderIsConfirmed).isEqualTo(orderDTO.orderIsConfirmed)
    }

    @Test
    fun `Map entity to order`() {
        val orderEntity = minRandom<OrderEntity>()
        val order = orderMapper.toDTO(orderEntity)

        assertThat(order.id).isEqualTo(orderEntity.id)
        assertThat(order.createdAt).isEqualTo(orderEntity.createdAt)
    }
}
