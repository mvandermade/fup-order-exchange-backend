package com.example.stamp.mappers

import com.example.stamp.controllers.responses.exceptions.ResponseExceptionResponse
import com.example.stamp.exceptions.ResponseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class ExceptionMapper(
    private val objectMapper: ObjectMapper,
) {
    fun toResponseBody(e: ResponseException): String {
        return objectMapper.writeValueAsString(
            ResponseExceptionResponse(
                e.httpStatus.value(),
                e.message,
                e.origin,
                e.originId,
            ),
        )
    }
}
