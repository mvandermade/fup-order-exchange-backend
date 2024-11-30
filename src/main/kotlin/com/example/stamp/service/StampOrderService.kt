package com.example.stamp.service

import com.example.stamp.entity.Order
import com.example.stamp.entity.Stamp
import com.example.stamp.exception.WaitingForStampException
import com.example.stamp.repository.OrderRepository
import com.example.stamp.repository.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StampOrderService(
    private val stampRepository: StampRepository,
    private val orderRepository: OrderRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun attachStampsToOrder(order: Order): Stamp? {
        if (order.stamp != null) {
            logger.info("Already attached order: ${order.id}")
            return order.stamp
        }

        logger.info("Attempt to attach order: ${order.id}")
        val stamp = stampRepository.getFirstByOrderIdIsNull()
        if (stamp == null) {
            logger.warn("No stamps left in the database")
            throw WaitingForStampException(order.id)
        }

        // Version 0 proves right to the postzegel, fresh ones are null
        stamp.version = 0
        stamp.order = order

        return stampRepository.save(stamp)
    }

    fun attachStampsToAny() {
        logger.info("Attempt to grab stamp without order")
        val stamp = stampRepository.getFirstByOrderIdIsNull()
        if (stamp == null) {
            logger.warn("No stamps left in the database")
            return
        }

        val order = orderRepository.getReferenceFirstByOrderConfirmedIsTrueAndStampIsNull()

        if (order == null) {
            logger.info("No open orders, going to sleep")
            return
        }

        // Version 0 proves right to the postzegel, fresh ones are null
        stamp.version = 0
        stamp.order = order

        stampRepository.save(stamp)
        logger.info("Stamp attached!")
    }
}
