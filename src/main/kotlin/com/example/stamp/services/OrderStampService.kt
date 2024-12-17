package com.example.stamp.services

import com.example.stamp.entities.OrderEntity
import com.example.stamp.entities.OrderStampEntity
import com.example.stamp.entities.StampEntity
import com.example.stamp.exceptions.WaitingForStampException
import com.example.stamp.mappers.StampMapper
import com.example.stamp.models.Stamp
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class OrderStampService(
    private val stampRepository: StampRepository,
    private val orderRepository: OrderRepository,
    private val orderStampRepository: OrderStampRepository,
    private val stampMapper: StampMapper,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(rollbackFor = [Exception::class])
    fun attachStampsToOrder(orderEntity: OrderEntity): Stamp {
        logger.info("Attach stamp to order: ${orderEntity.id}")
        val stamp = stampRepository.findFirstByOrderStampEntityIsNull()

        if (stamp == null) {
            logger.warn("Attach failed, no stamps left in the database for user")
            throw WaitingForStampException(orderEntity.id)
        }

        attemptToLink(orderEntity, stamp)

        return stampMapper.toStamp(stamp)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun attachStampsToEarliestCreatedAt() {
        val order =
            orderRepository.getReferenceFirstByOrderConfirmedIsTrueAndOrderStampEntityIsNullOrderByCreatedAtAsc()
        if (order == null) {
            logger.info("No open orders, going to sleep")
            return
        }
        attachStampsToOrder(order)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = [Exception::class])
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
