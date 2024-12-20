package com.example.stamp.controllers

import com.example.stamp.controllers.requests.ReportRequest
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
class StampsController(
    private val stampService: StampService,
    private val stampMapper: StampMapper,
) {
    @GetMapping("/collect/{orderId}")
    fun collectStamp(
        @PathVariable orderId: Long,
    ) = ResponseEntity.ok(
        stampMapper.toResponse(
            stampService.attemptStampCollection(orderId),
        ),
    )

    @PutMapping("/report")
    fun putReport(
        @RequestBody reportRequest: ReportRequest,
    ): ResponseEntity<Unit> {
        return ResponseEntity.accepted().build()
    }
}
