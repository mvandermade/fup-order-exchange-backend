package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

open class ResponseV1Exception(
    override val message: String = "",
    val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val origin: String = "",
    val originId: String = "",
) : Throwable()
