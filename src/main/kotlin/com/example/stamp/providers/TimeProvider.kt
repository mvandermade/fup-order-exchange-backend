package com.example.stamp.providers

import org.springframework.stereotype.Component

@Component
class TimeProvider {
    fun currentTimeMillis() = System.currentTimeMillis()
}
