package com.example.stamp.services

import com.example.stamp.entities.Order
import com.example.stamp.repositories.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
) {
    fun requestOrder(): Order {
        return orderRepository.save(Order())
    }
}
