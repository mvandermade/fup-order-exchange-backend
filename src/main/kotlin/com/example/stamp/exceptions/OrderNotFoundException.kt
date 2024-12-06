package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class OrderNotFoundException(origin: Long) : ResponseException(
    "Order not found",
    HttpStatus.NOT_FOUND,
    "orderId",
    origin.toString(),
)
