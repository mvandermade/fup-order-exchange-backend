package com.example.stamp.controllers

import com.example.stamp.controllers.requests.OrderConfirmRequest
import com.example.stamp.services.ConfirmationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/confirmations")
class ConfirmationsController(
    private val confirmationService: ConfirmationService,
) {
    // Confirming allows the end user to checkpoint/retransmit in case of failure.
    @PutMapping
    fun confirm(
        @RequestBody request: OrderConfirmRequest,
    ) = ResponseEntity.ok(confirmationService.confirm(request))
}
