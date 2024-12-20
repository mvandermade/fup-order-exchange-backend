package com.example.stamp.services

import com.example.stamp.exceptions.OrderNotFoundV1Exception
import com.example.stamp.repositories.OrderRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AcknowledgementsService(
    private val orderRepository: OrderRepository,
) {
    fun acknowledge(orderId: Long) {
        val order =
            orderRepository.findByIdOrNull(orderId)
                ?: throw OrderNotFoundV1Exception(orderId)

        orderRepository.save(order.apply { orderIsAcknowledged = true })
    }
}
