package com.example.stamp.controllers.responses

import java.time.Instant

data class OrderResponse(val orderId: Long, val createdAt: Instant?)
