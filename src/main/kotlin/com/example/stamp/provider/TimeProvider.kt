package com.example.stamp.provider

import org.springframework.stereotype.Component

@Component
class TimeProvider {
    fun currentTimeMillis() = System.currentTimeMillis()
}
