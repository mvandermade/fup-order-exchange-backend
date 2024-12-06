package com.example.stamp.services

import com.example.stamp.controllers.requests.OrderConfirmRequest
import com.example.stamp.exceptions.OrderNotFoundException
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
                ?: throw OrderNotFoundException(request.orderId)

        orderRepository.save(order.apply { orderConfirmed = true })
    }
}
