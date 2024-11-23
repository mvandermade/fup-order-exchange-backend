package com.example.stamp.controller

import com.example.stamp.domain.Order
import com.example.stamp.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val orderService: OrderService,
) {
    @GetMapping("")
    fun getStamp(): Order? {
        return orderService.genAndCoupleOrder()
    }

    @GetMapping("/order/{orderId}")
    fun getOrder(
        @PathVariable orderId: String,
    ): Order {
        return orderService.getOrderFor(orderId)
    }
}
