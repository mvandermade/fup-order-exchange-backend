package com.example.stamp.services

import com.example.stamp.datatransferobjects.StampDTO
import com.example.stamp.exceptions.OrderNotConfirmedV1Exception
import com.example.stamp.exceptions.OrderNotFoundV1Exception
import com.example.stamp.mappers.StampMapper
import com.example.stamp.repositories.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StampService(
    private val orderRepository: OrderRepository,
    private val orderStampService: OrderStampService,
    private val stampMapper: StampMapper,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun attemptStampCollection(orderId: Long): StampDTO {
        val order = orderRepository.findByIdOrNull(orderId) ?: throw OrderNotFoundV1Exception(orderId)
        if (!order.orderIsConfirmed) {
            throw OrderNotConfirmedV1Exception(orderId)
        }

        if (order.orderStampEntity?.stampEntity != null) {
            logger.info("Serving stamp from database")
        } else {
            logger.info("Trying to attach stamp immediately...")
        }

        val stamp =
            order.orderStampEntity?.stampEntity?.let { stampMapper.toDTO(it) }
                ?: orderStampService.attachStampsToOrder(order)

        return stamp
    }
}
