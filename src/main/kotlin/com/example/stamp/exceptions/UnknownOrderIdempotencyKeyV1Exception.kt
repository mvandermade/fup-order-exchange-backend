package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class UnknownOrderIdempotencyKeyV1Exception(origin: Long) : ResponseV1Exception(
    "Unknown idempotency key given.",
    HttpStatus.NOT_ACCEPTABLE,
    "idempotency key",
    origin.toString(),
    ErrorCode.ORDER_IDEMPOTENCY_KEY_NOT_FOUND,
)
