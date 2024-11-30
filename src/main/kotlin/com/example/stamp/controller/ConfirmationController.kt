package com.example.stamp.controller

import com.example.stamp.controller.request.OrderConfirmRequest
import com.example.stamp.service.ConfirmationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/confirmations")
class ConfirmationController(
    private val confirmationService: ConfirmationService,
) {
    // Confirming allows the end user to checkpoint/retransmit in case of failure.
    @PutMapping
    fun confirm(
        @RequestBody request: OrderConfirmRequest,
    ) = ResponseEntity.ok(confirmationService.confirm(request))
}
