package com.example.stamp.provider

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UUIDProvider {
    fun getUUID() = UUID.randomUUID()
}
