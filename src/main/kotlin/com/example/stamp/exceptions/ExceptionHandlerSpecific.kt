package com.example.stamp.exceptions

import com.example.stamp.mappers.ExceptionMapper
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class ExceptionHandlerSpecific(
    private val exceptionMapper: ExceptionMapper,
) {
    @ExceptionHandler(ResponseException::class)
    fun defaultHandler(e: ResponseException): ResponseEntity<String> {
        return ResponseEntity
            .status(e.httpStatus)
            .body(exceptionMapper.toResponseBody(e))
    }
}
