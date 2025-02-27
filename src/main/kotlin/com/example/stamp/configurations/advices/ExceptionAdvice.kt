package com.example.stamp.configurations.advices

import com.example.stamp.mappers.ExceptionMapper
import io.swagger.v3.oas.annotations.Hidden
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Profile("!development")
@ControllerAdvice
@Hidden
class ExceptionAdvice(
    private val exceptionMapper: ExceptionMapper,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResponseEntity<String> {
        logger.warn("Error has occurred in controller", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(exceptionMapper.toGenericException())
    }
}
