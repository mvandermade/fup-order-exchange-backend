package com.example.stamp.services

import com.example.stamp.controllers.requests.OrderV1Request
import com.example.stamp.dtos.OrderDTO
import com.example.stamp.entities.OrderEntity
import com.example.stamp.exceptions.OrderConfirmedV1Exception
import com.example.stamp.exceptions.OrderNotConfirmedV1Exception
import com.example.stamp.exceptions.OrderNotFoundV1Exception
import com.example.stamp.mappers.OrderMapper
import com.example.stamp.repositories.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderMapper: OrderMapper,
    private val orderStampService: OrderStampService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun postOrder(): OrderDTO {
        val entity = orderRepository.save(OrderEntity())
        val stampEntity = entity.orderStampEntity?.stampEntity
        return orderMapper.toDTO(entity, stampEntity)
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
        val updatedStampEntity = entity.orderStampEntity?.stampEntity
        return orderMapper.toDTO(updated, updatedStampEntity)
    }

    fun attemptStampCollection(orderId: Long): OrderDTO {
        val order = orderRepository.findByIdOrNull(orderId) ?: throw OrderNotFoundV1Exception(orderId)
        if (!order.orderIsConfirmed) {
            throw OrderNotConfirmedV1Exception(orderId)
        }

        if (order.orderStampEntity?.stampEntity != null) {
            logger.info("Serving stamp from database")
        } else {
            logger.info("Trying to attach stamp immediately...")
            orderStampService.attachStampsToOrderId(order.id)
        }

        val updatedOrder = orderRepository.findByIdOrNull(orderId) ?: throw OrderNotFoundV1Exception(orderId)
        val updatedStamp = updatedOrder.orderStampEntity?.stampEntity
        return orderMapper.toDTO(updatedOrder, updatedStamp)
    }
}
