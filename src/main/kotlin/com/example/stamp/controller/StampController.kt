package com.example.stamp.controller

import com.example.stamp.controller.response.StampResponse
import com.example.stamp.service.StampService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/stamps")
class StampController(
    private val stampService: StampService,
) {
    @GetMapping("/collect/{orderId}")
    fun getStamp(
        @PathVariable orderId: Long,
    ): StampResponse? {
        return stampService.attemptStampCollection(orderId)
    }
}
