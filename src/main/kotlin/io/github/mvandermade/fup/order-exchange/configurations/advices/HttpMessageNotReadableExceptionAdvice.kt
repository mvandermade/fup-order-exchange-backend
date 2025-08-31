package io.github.mvandermade.fup.`order-exchange`.configurations.advices

import com.example.stamp.mappers.ExceptionMapper
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Hidden
class HttpMessageNotReadableExceptionAdvice(
    private val exceptionMapper: ExceptionMapper,
) {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handle(e: HttpMessageNotReadableException): ResponseEntity<String> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(exceptionMapper.toResponseBody(e))
}
