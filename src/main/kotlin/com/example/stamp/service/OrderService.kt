package com.example.stamp.service

import com.example.stamp.domain.OrderResponse
import com.example.stamp.entity.Order
import com.example.stamp.mapper.OrderMapper
import com.example.stamp.repository.OrderRepository
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
