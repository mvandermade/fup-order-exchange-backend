package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class OrderNotConfirmedException(origin: Long) : ResponseException(
    "Order not confirmed, use confirmations endpoint first. It allows for idempotency.",
    HttpStatus.NOT_ACCEPTABLE,
    "orderId",
    origin.toString(),
)
