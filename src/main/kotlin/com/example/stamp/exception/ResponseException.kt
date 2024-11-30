package com.example.stamp.exception

import org.springframework.http.HttpStatus

open class ResponseException(
    val httpStatus: HttpStatus,
    val origin: String,
) : Throwable()
