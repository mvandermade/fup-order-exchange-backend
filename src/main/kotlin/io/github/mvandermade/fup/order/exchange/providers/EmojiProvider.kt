package io.github.mvandermade.fup.order.exchange.providers

import com.example.stamp.providers.constants.emojis
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class EmojiProvider {
    private val logger = LoggerFactory.getLogger(javaClass)

    val counter = AtomicLong(0)

    fun nextLong(to: Int): Int = (counter.getAndIncrement() % to).toInt()

    fun randomEmoji(): String {
        logger.warn("Randomization is currently disabled for testing purposes")
        return emojis[nextLong(emojis.size)]
    }
}
