package io.github.mvandermade.fup.order.exchange.mappers

import io.github.mvandermade.fup.order.exchange.controllers.responses.OrderV1Response
import io.github.mvandermade.fup.order.exchange.dtos.OrderDTO
import io.github.mvandermade.fup.order.exchange.entities.OrderEntity
import io.github.mvandermade.fup.order.exchange.entities.StampEntity
import org.springframework.stereotype.Component

@Component
class OrderMapper(
    val stampMapper: StampMapper,
) {
    fun toResponse(orderDTO: OrderDTO): OrderV1Response =
        OrderV1Response(
            id = orderDTO.id,
            stamp = orderDTO.stamp,
        )

    fun toDTO(
        orderEntity: OrderEntity,
        stampEntity: StampEntity?,
    ): OrderDTO =
        OrderDTO(
            orderEntity.id,
            orderEntity.createdAt,
            stamp = stampEntity?.let { stampMapper.toDTO(it) },
        )
}
