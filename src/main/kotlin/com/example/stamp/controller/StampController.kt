package com.example.stamp.controller

import com.example.stamp.domain.StampResponse
import com.example.stamp.service.OrderService
import com.example.stamp.service.StampService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StampController(
    private val stampService: StampService,
    private val orderService: OrderService
) {
    @GetMapping("")
    fun getStamp(): StampResponse? {
        val orderDTO = orderService.requestOrder()
        return stampService.attemptStampCollection(orderDTO)
    }
}
