package com.example.stamp.daemons

import com.example.stamp.entities.StampEntity
import com.example.stamp.providers.RandomProvider
import com.example.stamp.providers.TransactionProvider
import com.example.stamp.repositories.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class StampGeneratorDaemon(
    private val stampRepository: StampRepository,
    private val randomProvider: RandomProvider,
    private val transactionProvider: TransactionProvider,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 1_000, initialDelay = 2_000)
    fun insertCodes() {
        try {
            transactionProvider.newReadWrite {
                persistRandomStamp()
            }
        } catch (e: Exception) {
            if (e.message?.contains("Unique index or primary key violation:") == true) {
                logger.info("Random stamp was duplicated")
            } else {
                e.printStackTrace()
            }
        }
    }

    fun persistRandomStamp() {
        val code = randomProvider.randomString(1)

        // Prevent unneeded attempts cluttering the logs
        if (stampRepository.findByCode(code) != null) return

        val entity =
            StampEntity(
                code = code,
            )
        stampRepository.save(entity)
    }
}
