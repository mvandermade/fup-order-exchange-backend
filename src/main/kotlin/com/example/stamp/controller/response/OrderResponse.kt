package com.example.stamp.controller.response

import java.time.Instant

data class OrderResponse(val orderId: Long, val createdAt: Instant?)
