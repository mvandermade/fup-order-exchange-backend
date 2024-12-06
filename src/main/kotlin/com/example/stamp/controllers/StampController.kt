package com.example.stamp.controllers

import com.example.stamp.exceptions.ResponseException
import com.example.stamp.mappers.ExceptionMapper
import com.example.stamp.services.StampService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/stamps")
class StampController(
    private val stampService: StampService,
    private val exceptionMapper: ExceptionMapper,
) {
    @GetMapping("/collect/{orderId}")
    fun getStamp(
        @PathVariable orderId: Long,
    ) = ResponseEntity.ok(stampService.attemptStampCollection(orderId))

    @ExceptionHandler(ResponseException::class)
    fun defaultHandler(e: ResponseException): ResponseEntity<String> {
        return ResponseEntity
            .status(e.httpStatus)
            .body(exceptionMapper.toResponseBody(e))
    }
}
