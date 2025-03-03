package com.example.stamp.providers

import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class TimeProvider {
    fun offsetDateTime(): OffsetDateTime = OffsetDateTime.now()
}
