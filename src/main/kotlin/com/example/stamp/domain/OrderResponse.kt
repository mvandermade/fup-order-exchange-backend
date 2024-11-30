package com.example.stamp.domain

import java.time.Instant

data class OrderResponse(val orderId: Long, val instant: Instant?)
