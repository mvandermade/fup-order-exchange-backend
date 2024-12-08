package com.example.stamp.services

import com.example.stamp.entities.Order
import com.example.stamp.entities.Stamp
import com.example.stamp.exceptions.WaitingForStampException
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StampOrderService(
    private val stampRepository: StampRepository,
    private val orderRepository: OrderRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun attachStampsToOrder(order: Order): Stamp {
        logger.info("Attach stamp to order: ${order.id}")
        val stamp = stampRepository.findFirstByOrderIsNull()

        if (stamp == null) {
            logger.warn("Attach failed, no stamps left in the database for user")
            throw WaitingForStampException(order.id)
        }

        return attemptToLink(order, stamp)
    }

    fun attachStampsToEarliestCreatedAt() {
        val order =
            orderRepository.getReferenceFirstByOrderConfirmedIsTrueAndStampIsNullOrderByCreatedAtAsc()
        if (order == null) {
            logger.info("No open orders, going to sleep")
            return
        }
        attachStampsToOrder(order)
    }

    fun attemptToLink(
        order: Order,
        stamp: Stamp,
    ): Stamp {
        // Version 0 proves right to the postzegel, fresh ones are null
        stamp.version = 0
        stamp.order = order

        val linkedStamp = stampRepository.save(stamp)
        logger.info("Stamp attached!")
        return linkedStamp
    }
}
