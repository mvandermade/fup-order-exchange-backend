package com.example.stamp.mapper

import com.example.stamp.controller.response.OrderResponse
import com.example.stamp.entity.Order
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
