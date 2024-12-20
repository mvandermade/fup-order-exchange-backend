package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class OrderNotConfirmedV1Exception(origin: Long) : ResponseV1Exception(
    "Order not confirmed, use confirmations endpoint first. It allows for idempotency.",
    HttpStatus.NOT_ACCEPTABLE,
    "orderId",
    origin.toString(),
)
