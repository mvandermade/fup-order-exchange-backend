package com.example.stamp.daemon

import com.example.stamp.repository.OrderRepository
import com.example.stamp.repository.StampRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class StampClaimDaemon(
    private val stampRepository: StampRepository,
    private val orderRepository: OrderRepository,
) {
    @Scheduled(fixedDelay = 20_000, initialDelay = 10_000)
    fun attachStampsToOrders() {
        val stamp =
            stampRepository.getFirstByOrderIdIsNull()
                ?: return

        val order =
            orderRepository.getFirstByCpConfirmedIsTrueAndStampIsNull()
                ?: return

        // Version 0 proves right to the postzegel, fresh ones are null
        stamp.version = 0
        stamp.order = order
        // Flush to expect DataIntegrityViolation
        stampRepository.save(stamp)
        // Other side of relation
        order.stamp = stamp
        orderRepository.save(order)

        println("Stamp saved!")
    }
}
