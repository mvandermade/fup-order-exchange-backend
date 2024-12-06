package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class StampNotFoundException(origin: Long) : ResponseException(
    "Stamp not found",
    HttpStatus.NOT_FOUND,
    "orderId",
    origin.toString(),
)
