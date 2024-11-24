package com.example.stamp.mapper

import com.example.stamp.domain.OrderDTO
import com.example.stamp.entity.Order
import org.springframework.stereotype.Component

@Component
class OrderMapper {
    fun toOrder(order: Order): OrderDTO {
        return OrderDTO(
            order.id, order.createDate
        )
    }
}