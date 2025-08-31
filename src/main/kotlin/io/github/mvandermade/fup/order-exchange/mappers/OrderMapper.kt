package io.github.mvandermade.fup.`order-exchange`.mappers

import com.example.stamp.controllers.responses.OrderV1Response
import com.example.stamp.dtos.OrderDTO
import com.example.stamp.entities.OrderEntity
import com.example.stamp.entities.StampEntity
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
