package com.example.stamp.daemon

import com.example.stamp.service.StampOrderService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class StampClaimDaemon(
    private val stampOrderService: StampOrderService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelay = 20_000, initialDelay = 10_000)
    fun lookForStamps() {
        logger.info("Attempt to claim loose stamps...")
        stampOrderService.attachStampsToAny()
    }
}
