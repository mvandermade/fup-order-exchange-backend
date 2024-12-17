package com.example.stamp.services

import com.example.stamp.entities.OrderEntity
import com.example.stamp.mappers.OrderMapper
import com.example.stamp.models.Order
import com.example.stamp.repositories.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderMapper: OrderMapper,
) {
    fun requestOrder(): Order {
        val entity = orderRepository.save(OrderEntity())
        return orderMapper.toOrder(entity)
    }
}
