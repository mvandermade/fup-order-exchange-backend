package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class OrderNotAcknowledgedV1Exception(origin: Long) : ResponseV1Exception(
    "Order not acknowledgement, use acknowledgements endpoint first. It allows for client idempotency.",
    HttpStatus.NOT_ACCEPTABLE,
    "orderId",
    origin.toString(),
)
