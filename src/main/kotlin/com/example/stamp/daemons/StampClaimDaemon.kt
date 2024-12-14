package com.example.stamp.daemons

import com.example.stamp.services.OrderStampService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Profile("!test")
class StampClaimDaemon(
    private val orderStampService: OrderStampService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelay = 20_000, initialDelay = 10_000)
    @Transactional(rollbackFor = [Exception::class])
    fun lookForStamps() {
        logger.info("Attempt to claim loose stamps...")
        orderStampService.attachStampsToEarliestCreatedAt()
    }
}
