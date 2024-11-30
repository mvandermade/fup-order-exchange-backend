package com.example.stamp.controller

import com.example.stamp.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/orders")
class OrderController(
    private val orderService: OrderService,
) {
    // TODO expand this to also accept more interesting things. For now we order 1 stamp.
    @PostMapping
    fun requestOrder() = ResponseEntity.ok(orderService.requestOrder())
}
