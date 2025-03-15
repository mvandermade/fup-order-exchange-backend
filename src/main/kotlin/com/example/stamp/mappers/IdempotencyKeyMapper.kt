package com.example.stamp.mappers

import com.example.stamp.dtos.OrderIdempotencyKeyDTO
import com.example.stamp.entities.OrderIdempotencyKeyEntity
import org.springframework.stereotype.Component

@Component
class IdempotencyKeyMapper {
    fun toDTO(entity: OrderIdempotencyKeyEntity): OrderIdempotencyKeyDTO {
        return OrderIdempotencyKeyDTO(
            id = entity.id,
            orderId = entity.order.id,
        )
    }
}
