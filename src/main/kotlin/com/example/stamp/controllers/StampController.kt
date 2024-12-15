package com.example.stamp.controllers

import com.example.stamp.mappers.ExceptionMapper
import com.example.stamp.mappers.StampMapper
import com.example.stamp.services.StampService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/stamps")
class StampController(
    private val stampService: StampService,
    private val exceptionMapper: ExceptionMapper,
    private val stampMapper: StampMapper,
) {
    @GetMapping("/collect/{orderId}")
    fun getStamp(
        @PathVariable orderId: Long,
    ) = ResponseEntity.ok(
        stampMapper.toResponse(
            stampService.attemptStampCollection(orderId),
        ),
    )
}
