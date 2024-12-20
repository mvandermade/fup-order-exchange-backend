package com.example.stamp.controllers

import com.example.stamp.controllers.requests.ReportV1Request
import com.example.stamp.mappers.StampMapper
import com.example.stamp.services.StampService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/stamps")
class StampsV1Controller(
    private val stampService: StampService,
    private val stampMapper: StampMapper,
) {
    @GetMapping("/collect/{orderId}")
    fun collectStamp(
        @PathVariable orderId: Long,
    ) = ResponseEntity.ok(stampMapper.toResponse(stampService.attemptStampCollection(orderId)))

    @PutMapping("/report")
    fun putReport(
        @RequestBody reportV1Request: ReportV1Request,
    ) = ResponseEntity.accepted().build<Unit>()
}
