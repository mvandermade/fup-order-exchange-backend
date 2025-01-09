package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class OrderNotConfirmedV1Exception(origin: Long) : ResponseV1Exception(
    "Order not confirmed, PUT it first.",
    HttpStatus.NOT_ACCEPTABLE,
    "orderId",
    origin.toString(),
    ErrorCode.ORDER_NOT_CONFIRMED,
)
