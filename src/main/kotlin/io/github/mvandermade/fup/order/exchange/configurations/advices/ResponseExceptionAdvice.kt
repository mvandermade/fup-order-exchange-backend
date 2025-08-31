package io.github.mvandermade.fup.order.exchange.configurations.advices

import io.github.mvandermade.fup.order.exchange.exceptions.ResponseV1Exception
import io.github.mvandermade.fup.order.exchange.mappers.ExceptionMapper
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Hidden
class ResponseExceptionAdvice(
    private val exceptionMapper: ExceptionMapper,
) {
    @ExceptionHandler(ResponseV1Exception::class)
    fun handle(e: ResponseV1Exception): ResponseEntity<String> =
        ResponseEntity
            .status(e.httpStatus)
            .body(exceptionMapper.toResponseBody(e))
}
