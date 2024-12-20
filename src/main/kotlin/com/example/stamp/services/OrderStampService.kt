package com.example.stamp.services

import com.example.stamp.datatransferobjects.StampDTO
import com.example.stamp.entities.OrderEntity
import com.example.stamp.entities.OrderStampEntity
import com.example.stamp.entities.StampEntity
import com.example.stamp.exceptions.WaitingForStampV1Exception
import com.example.stamp.mappers.StampMapper
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OrderStampService(
    private val stampRepository: StampRepository,
    private val orderRepository: OrderRepository,
    private val orderStampRepository: OrderStampRepository,
    private val stampMapper: StampMapper,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun attachStampsToOrder(orderEntity: OrderEntity): StampDTO {
        logger.info("Attach stamp to order: ${orderEntity.id}")
        val stamp = stampRepository.findFirstByOrderStampEntityIsNull()

        if (stamp == null) {
            logger.warn("Attach failed, no stamps left in the database for user")
            throw WaitingForStampV1Exception(orderEntity.id)
        }

        attemptToLink(orderEntity, stamp)

        return stampMapper.toDTO(stamp)
    }

    fun attachStampsToEarliestCreatedAt() {
        val order =
            orderRepository.getReferenceFirstByOrderIsAcknowledgedIsTrueAndOrderStampEntityIsNullOrderByCreatedAtAsc()
                ?: return
        attachStampsToOrder(order)
    }

    fun attemptToLink(
        orderEntity: OrderEntity,
        stampEntity: StampEntity,
    ) {
        orderStampRepository.save(
            OrderStampEntity(orderEntity, stampEntity),
        )
        logger.info("Stamp attached!")
    }
}
