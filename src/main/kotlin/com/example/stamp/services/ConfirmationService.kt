package com.example.stamp.services

import com.example.stamp.controllers.requests.OrderConfirmRequest
import com.example.stamp.exceptions.OrderNotFoundV1Exception
import com.example.stamp.repositories.OrderRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ConfirmationService(
    private val orderRepository: OrderRepository,
) {
    fun confirm(request: OrderConfirmRequest) {
        val order =
            orderRepository.findByIdOrNull(request.orderId)
                ?: throw OrderNotFoundV1Exception(request.orderId)

        orderRepository.save(order.apply { orderConfirmed = true })
    }
}
