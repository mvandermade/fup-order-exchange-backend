package com.example.stamp.providers

import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class TimeProvider {
    fun currentTimeMillis() = System.currentTimeMillis()

    fun offsetDateTime(): OffsetDateTime = OffsetDateTime.now()
}
