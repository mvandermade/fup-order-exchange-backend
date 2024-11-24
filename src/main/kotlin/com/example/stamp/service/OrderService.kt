package com.example.stamp.service

import com.example.stamp.domain.OrderDTO
import com.example.stamp.entity.Order
import com.example.stamp.mapper.OrderMapper
import com.example.stamp.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderMapper: OrderMapper,
    private val orderRepository: OrderRepository
) {
    fun requestOrder(): OrderDTO {
        val order = orderRepository.save(
            Order()
        )

        return orderMapper.toOrder(order)
    }
}