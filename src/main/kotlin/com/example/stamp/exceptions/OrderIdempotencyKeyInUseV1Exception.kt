package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class OrderIdempotencyKeyInUseV1Exception(origin: Long) : ResponseV1Exception(
    "Idempotency key is in use.",
    HttpStatus.NOT_ACCEPTABLE,
    "idempotency key",
    origin.toString(),
    ErrorCode.ORDER_IDEMPOTENCY_KEY_IN_USE,
)
