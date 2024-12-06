package com.example.stamp.mappers

import com.example.stamp.controllers.responses.OrderResponse
import com.example.stamp.entities.Order
import org.springframework.stereotype.Component

@Component
class OrderMapper {
    fun toResponse(order: Order): OrderResponse {
        return OrderResponse(
            order.id,
            order.createdAt,
        )
    }
}
