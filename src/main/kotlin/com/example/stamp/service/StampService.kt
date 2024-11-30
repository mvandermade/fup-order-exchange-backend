package com.example.stamp.service

import com.example.stamp.controller.response.StampResponse
import com.example.stamp.exception.OrderNotFoundException
import com.example.stamp.mapper.StampMapper
import com.example.stamp.repository.OrderFullRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StampService(
    private val stampMapper: StampMapper,
    private val orderRepository: OrderFullRepository,
    private val stampOrderService: StampOrderService,
) {
    fun attemptStampCollection(orderId: Long): StampResponse? {
        val order = orderRepository.findByIdOrNull(orderId) ?: throw OrderNotFoundException(orderId)
        val stamp = stampOrderService.attachStampsToOrder(order)
        return stamp?.let { stampMapper.toResponse(it) }
    }
}
