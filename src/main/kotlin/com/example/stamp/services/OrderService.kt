package com.example.stamp.services

import com.example.stamp.controllers.responses.OrderResponse
import com.example.stamp.entities.Order
import com.example.stamp.mappers.OrderMapper
import com.example.stamp.repositories.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderMapper: OrderMapper,
    private val orderRepository: OrderRepository,
) {
    fun requestOrder(): OrderResponse {
        val order =
            orderRepository.save(
                Order(),
            )

        return orderMapper.toResponse(order)
    }
}
