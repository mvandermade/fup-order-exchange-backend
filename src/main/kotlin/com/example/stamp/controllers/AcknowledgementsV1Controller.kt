package com.example.stamp.controllers

import com.example.stamp.services.AcknowledgementsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Acknowledging allows the end user to checkpoint/retransmit in case of client delivery failure.
@RestController
@RequestMapping("/v1/acknowledgements")
class AcknowledgementsV1Controller(
    private val acknowledgementsService: AcknowledgementsService,
) {
    @PutMapping("/orders/{orderId}")
    fun acknowledge(
        @PathVariable("orderId") orderId: Long,
    ): ResponseEntity<Unit> {
        acknowledgementsService.acknowledge(orderId)
        return ResponseEntity.ok().build()
    }
}
