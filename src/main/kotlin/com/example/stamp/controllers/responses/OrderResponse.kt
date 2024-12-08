package com.example.stamp.controllers.responses

import java.time.OffsetDateTime

data class OrderResponse(val orderId: Long, val createdAt: OffsetDateTime?)
