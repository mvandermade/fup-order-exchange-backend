package com.example.stamp.mappers

import com.example.stamp.controllers.responses.OrderV1Response
import com.example.stamp.dtos.OrderDTO
import com.example.stamp.entities.OrderEntity
import com.example.stamp.entities.StampEntity
import org.springframework.stereotype.Component

@Component
class OrderMapper(
    val stampMapper: StampMapper,
) {
    fun toResponse(orderDTO: OrderDTO): OrderV1Response {
        return OrderV1Response(
            id = orderDTO.id,
            orderIsConfirmed = orderDTO.orderIsConfirmed,
            stamp = orderDTO.stamp,
        )
    }

    fun toDTO(
        orderEntity: OrderEntity,
        stampEntity: StampEntity?,
    ): OrderDTO {
        return OrderDTO(
            orderEntity.id,
            orderEntity.createdAt,
            orderEntity.orderIsConfirmed,
            stamp = stampEntity?.let { stampMapper.toDTO(it) },
        )
    }
}
