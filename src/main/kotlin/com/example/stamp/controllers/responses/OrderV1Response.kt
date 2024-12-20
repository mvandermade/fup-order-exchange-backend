package com.example.stamp.controllers.responses

import java.time.OffsetDateTime

data class OrderV1Response(val orderId: Long, val createdAt: OffsetDateTime?)
