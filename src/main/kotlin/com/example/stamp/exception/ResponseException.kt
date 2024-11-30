package com.example.stamp.exception

open class ResponseException(
    code: ExceptionCode,
    origin: String,
) : Throwable()
