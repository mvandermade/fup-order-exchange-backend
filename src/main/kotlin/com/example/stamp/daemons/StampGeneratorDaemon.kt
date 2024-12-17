package com.example.stamp.daemons

import com.example.stamp.entities.StampEntity
import com.example.stamp.providers.RandomProvider
import com.example.stamp.repositories.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class StampGeneratorDaemon(
    private val stampRepository: StampRepository,
    private val randomProvider: RandomProvider,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelay = 1_000, initialDelay = 2_000)
    @Transactional
    fun insertCodes() {
        logger.info("Attempt to generate...")
        try {
            persistRandomStamp()
        } catch (e: DataIntegrityViolationException) {
            logger.info("Random stamp was duplicated")
        }
        logger.info("Generated a stamp")
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun persistRandomStamp() {
        val entity =
            StampEntity(
                code = randomProvider.randomString(1),
            )
        stampRepository.save(entity)
    }
}
