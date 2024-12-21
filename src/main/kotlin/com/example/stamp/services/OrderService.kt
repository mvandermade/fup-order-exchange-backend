package com.example.stamp.services

import com.example.stamp.controllers.requests.OrderV1Request
import com.example.stamp.datatransferobjects.OrderDTO
import com.example.stamp.entities.OrderEntity
import com.example.stamp.exceptions.OrderConfirmedV1Exception
import com.example.stamp.exceptions.OrderNotFoundV1Exception
import com.example.stamp.mappers.OrderMapper
import com.example.stamp.repositories.OrderRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderMapper: OrderMapper,
) {
    fun postOrder(): OrderDTO {
        val entity = orderRepository.save(OrderEntity())
        return orderMapper.toDTO(entity)
    }

    fun putOrder(
        orderId: Long,
        orderRequest: OrderV1Request,
    ): OrderDTO {
        val entity =
            orderRepository.findByIdOrNull(orderId)
                ?: throw OrderNotFoundV1Exception(orderId)

        if (entity.orderIsConfirmed) throw OrderConfirmedV1Exception(orderId)

        // Update
        entity.orderIsConfirmed = orderRequest.orderIsConfirmed

        val updated = orderRepository.save(entity)
        return orderMapper.toDTO(updated)
    }
}
