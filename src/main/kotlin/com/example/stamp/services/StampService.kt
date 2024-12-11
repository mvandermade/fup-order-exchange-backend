package com.example.stamp.services

import com.example.stamp.entities.Stamp
import com.example.stamp.exceptions.OrderNotConfirmedException
import com.example.stamp.exceptions.OrderNotFoundException
import com.example.stamp.repositories.OrderFullRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StampService(
    private val orderRepository: OrderFullRepository,
    private val stampOrderService: StampOrderService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun attemptStampCollection(orderId: Long): Stamp {
        val order = orderRepository.findByIdOrNull(orderId) ?: throw OrderNotFoundException(orderId)
        if (!order.orderConfirmed) {
            throw OrderNotConfirmedException(orderId)
        }

        if (order.stamp != null) {
            logger.info("Serving stamp from database")
        } else {
            logger.info("Trying to attach stamp immediately...")
        }

        return order.stamp ?: stampOrderService.attachStampsToOrder(order)
    }
}
