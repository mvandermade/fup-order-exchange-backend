package com.example.stamp.controllers.responses.exceptions

import com.example.stamp.exceptions.ErrorCode

data class ResponseExceptionResponse(
    // To proxies changing the code
    val httpStatus: Int,
    val message: String,
    val origin: String,
    val originId: String,
    val errorCode: ErrorCode,
    val service: String = "made-fp",
)
