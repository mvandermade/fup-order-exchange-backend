package com.example.stamp.daemon

import com.example.stamp.entity.StampEntity
import com.example.stamp.provider.TimeProvider
import com.example.stamp.repository.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class StampGeneratorDaemon(
    private val stampRepository: StampRepository,
    private val timeProvider: TimeProvider,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelay = 20_000, initialDelay = 2_000)
    fun insertCodes() {
        logger.info("Generating a stamp...")

        try {
            logger.info(persistRandomStamp())
        } catch (e: RuntimeException) {
            logger.info("Sleeping and retrying later!")
            // Check maybe to delete some expired ones ?
        }
    }

    private val charPool: List<Char> = ('A'..'B') + ('0'..'1')

    fun persistRandomStamp(): String {
        var didNotSucceed = 0

        while (didNotSucceed < 10) {
            try {
                val randomString =
                    (1..1)
                        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                        .map(charPool::get)
                        .joinToString("")
                return persistPostzegel(randomString).code
            } catch (e: DataIntegrityViolationException) {
                didNotSucceed++
            }
        }

        throw RuntimeException("Too many exceptions during code generation")
    }

    fun persistPostzegel(randomCode: String): StampEntity {
        val entity =
            StampEntity().apply {
                version = null // Creates a new row
                code = randomCode
                timeMillis = timeProvider.currentTimeMillis()
            }
        return stampRepository.save(entity)
    }
}
