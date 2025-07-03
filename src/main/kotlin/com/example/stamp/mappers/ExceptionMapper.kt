package com.example.stamp.mappers

import com.example.stamp.controllers.responses.exceptions.ResponseExceptionResponse
import com.example.stamp.exceptions.ErrorCode
import com.example.stamp.exceptions.ResponseV1Exception
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component

@Component
class ExceptionMapper(
    private val objectMapper: ObjectMapper,
) {
    fun toResponseBody(e: ResponseV1Exception): String {
        return objectMapper.writeValueAsString(
            ResponseExceptionResponse(
                httpStatus = e.httpStatus.value(),
                message = e.message,
                origin = e.origin,
                originId = e.originId,
                errorCode = e.errorCode,
            ),
        )
    }

    @Suppress("unused")
    fun toResponseBody(e: HttpMessageNotReadableException): String {
        return objectMapper.writeValueAsString(
            ResponseExceptionResponse(
                httpStatus = HttpStatus.BAD_REQUEST.value(),
                message = "Missing value",
                origin = "",
                originId = "",
                errorCode = ErrorCode.MISSING_VALUE,
            ),
        )
    }

    fun toGenericException(): String {
        return objectMapper.writeValueAsString(
            ResponseExceptionResponse(
                500,
                "Something went wrong",
                "Unknown",
                "Unknown",
                ErrorCode.UNKNOWN_ERROR,
            ),
        )
    }
}
