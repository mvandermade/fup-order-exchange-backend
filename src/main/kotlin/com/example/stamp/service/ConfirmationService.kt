package com.example.stamp.service

import com.example.stamp.controller.request.OrderConfirmRequest
import com.example.stamp.exception.OrderNotFoundException
import com.example.stamp.repository.OrderRepository
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

        orderRepository.save(order.apply { cpConfirmed = true })
    }
}
