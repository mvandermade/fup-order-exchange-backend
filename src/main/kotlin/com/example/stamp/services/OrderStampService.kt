package com.example.stamp.services

import com.example.stamp.entities.Order
import com.example.stamp.entities.OrderStamp
import com.example.stamp.entities.Stamp
import com.example.stamp.exceptions.WaitingForStampException
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
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(rollbackFor = [Exception::class])
    fun attachStampsToOrder(order: Order): Stamp {
        logger.info("Attach stamp to order: ${order.id}")
        val stamp = stampRepository.findFirstByOrderStampIsNull()

        if (stamp == null) {
            logger.warn("Attach failed, no stamps left in the database for user")
            throw WaitingForStampException(order.id)
        }

        attemptToLink(order, stamp)

        return stamp
    }

    @Transactional(rollbackFor = [Exception::class])
    fun attachStampsToEarliestCreatedAt() {
        val order =
            orderRepository.getReferenceFirstByOrderConfirmedIsTrueAndOrderStampIsNullOrderByCreatedAtAsc()
        if (order == null) {
            logger.info("No open orders, going to sleep")
            return
        }
        attachStampsToOrder(order)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = [Exception::class])
    fun attemptToLink(
        order: Order,
        stamp: Stamp,
    ) {
        orderStampRepository.save(
            OrderStamp(order, stamp),
        )
        logger.info("Stamp attached!")
    }
}
