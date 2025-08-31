package io.github.mvandermade.fup.`order-exchange`.services

import com.example.stamp.entities.StampEntity
import com.example.stamp.providers.EmojiProvider
import com.example.stamp.repositories.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StampService(
    private val stampRepository: StampRepository,
    private val emojiProvider: EmojiProvider,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun persistRandomStamp() {
        val code = emojiProvider.randomEmoji()
        logger.debug("Trying to persist a random stamp $code")

        // Cannot catch key violations in the logs therefore check it like this:
        if (stampRepository.findByCode(code) != null) return

        val entity =
            StampEntity(
                code = code,
            )
        stampRepository.save(entity)
    }
}
