package com.example.stamp.dtos

data class OrderIdempotencyKeyDTO(
    val id: Long,
    val orderId: Long,
)
