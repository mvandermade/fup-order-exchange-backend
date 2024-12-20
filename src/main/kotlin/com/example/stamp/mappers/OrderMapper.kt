package com.example.stamp.mappers

import com.example.stamp.controllers.responses.OrderV1Response
import com.example.stamp.datatransferobjects.OrderDTO
import com.example.stamp.entities.OrderEntity
import org.springframework.stereotype.Component

@Component
class OrderMapper {
    fun toResponse(orderDTO: OrderDTO): OrderV1Response {
        return OrderV1Response(
            orderDTO.id,
            orderDTO.createdAt,
        )
    }

    fun toDTO(orderEntity: OrderEntity): OrderDTO {
        return OrderDTO(
            orderEntity.id,
            orderEntity.createdAt,
            orderEntity.orderConfirmed,
        )
    }
}
