package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class OrderConfirmedV1Exception(origin: Long) : ResponseV1Exception(
    "Order is confirmed, no modifications possible anymore.",
    HttpStatus.NOT_ACCEPTABLE,
    "orderId",
    origin.toString(),
    ErrorCode.ORDER_IS_CONFIRMED,
)
