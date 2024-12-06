package com.example.stamp.controllers.responses.exceptions

data class ResponseExceptionResponse(
    // To proxies changing the code
    val httpStatus: Int,
    val message: String,
    val origin: String,
    val originId: String,
)
