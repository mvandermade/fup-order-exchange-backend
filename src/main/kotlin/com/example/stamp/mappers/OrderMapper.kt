package com.example.stamp.mappers

import com.example.stamp.controllers.responses.OrderResponse
import com.example.stamp.entities.OrderEntity
import com.example.stamp.models.Order
import org.springframework.stereotype.Component

@Component
class OrderMapper {
    fun toResponse(order: Order): OrderResponse {
        return OrderResponse(
            order.id,
            order.createdAt,
        )
    }

    fun toOrder(orderEntity: OrderEntity): Order {
        return Order(
            orderEntity.id,
            orderEntity.createdAt,
            orderEntity.orderConfirmed,
        )
    }
}
