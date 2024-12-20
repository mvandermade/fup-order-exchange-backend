package com.example.stamp.datatransferobjects

import java.time.OffsetDateTime

data class OrderDTO(val id: Long, val createdAt: OffsetDateTime?, val orderIsAcknowledged: Boolean)
