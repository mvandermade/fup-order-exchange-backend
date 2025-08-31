package io.github.mvandermade.fup.order.exchange.mappers

import io.github.mvandermade.fup.order.exchange.dtos.OrderDTO
import io.github.mvandermade.fup.order.exchange.entities.OrderEntity
import io.github.mvandermade.fup.order.exchange.entities.StampEntity
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderMapperTest {
    private val stampMapper = StampMapper()
    private val orderMapper =
        OrderMapper(
            stampMapper = stampMapper,
        )

    @Test
    fun `Map order to response`() {
        val orderDTO = minRandom<OrderDTO>()
        val response = orderMapper.toResponse(orderDTO)

        assertThat(response.id).isEqualTo(orderDTO.id)
    }

    @Test
    fun `Map entity to order`() {
        val orderEntity = minRandom<OrderEntity>()
        val stampEntity = minRandom<StampEntity>()
        val orderDTO = orderMapper.toDTO(orderEntity, stampEntity)
        val stampDTO = stampMapper.toDTO(stampEntity)

        assertThat(orderDTO.id).isEqualTo(orderEntity.id)
        assertThat(orderDTO.createdAt).isEqualTo(orderEntity.createdAt)
        assertThat(orderDTO.stamp?.code).isEqualTo(stampDTO.code)
    }
}
