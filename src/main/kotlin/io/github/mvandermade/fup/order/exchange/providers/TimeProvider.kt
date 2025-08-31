package io.github.mvandermade.fup.order.exchange.providers

import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class TimeProvider {
    fun offsetDateTime(): OffsetDateTime = OffsetDateTime.now()
}
