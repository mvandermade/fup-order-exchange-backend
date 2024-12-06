package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class WaitingForStampException(origin: Long) : ResponseException(
    "Stamp collection is in progress",
    HttpStatus.TOO_EARLY,
    "orderId",
    origin.toString(),
)
