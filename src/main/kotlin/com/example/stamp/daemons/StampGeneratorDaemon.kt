package com.example.stamp.daemons

import com.example.stamp.entities.Stamp
import com.example.stamp.providers.RandomProvider
import com.example.stamp.providers.TimeProvider
import com.example.stamp.repositories.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class StampGeneratorDaemon(
    private val stampRepository: StampRepository,
    private val timeProvider: TimeProvider,
    private val randomProvider: RandomProvider,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelay = 120_000, initialDelay = 2_000)
    fun insertCodes() {
        logger.info("Attempt to generate...")
        try {
            persistRandomStamp()
        } catch (e: DataIntegrityViolationException) {
            logger.info("Random stamp was duplicated")
        }
        logger.info("Generated a stamp")
    }

    fun persistRandomStamp() {
        val entity =
            Stamp(
                code = randomProvider.randomString(1),
                timeMillis = timeProvider.currentTimeMillis(),
            )
        stampRepository.save(entity)
    }
}
