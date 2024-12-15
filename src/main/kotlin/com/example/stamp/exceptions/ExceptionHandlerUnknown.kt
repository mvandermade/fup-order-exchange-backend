package com.example.stamp.exceptions

import com.example.stamp.mappers.ExceptionMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerUnknown(
    private val exceptionMapper: ExceptionMapper,
) {
    @ExceptionHandler(Exception::class)
    fun unknownHandler(e: Exception): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(exceptionMapper.toGenericException())
    }
}
