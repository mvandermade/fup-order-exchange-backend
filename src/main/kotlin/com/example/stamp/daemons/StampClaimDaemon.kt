package com.example.stamp.daemons

import com.example.stamp.services.OrderStampService
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class StampClaimDaemon(
    private val orderStampService: OrderStampService,
) {
    @Scheduled(fixedDelay = 20_000, initialDelay = 10_000)
    fun lookForStamps() {
        orderStampService.attachStampsToEarliestCreatedAt()
    }
}
