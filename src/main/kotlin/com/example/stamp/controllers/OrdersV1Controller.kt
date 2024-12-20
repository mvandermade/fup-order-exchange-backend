package com.example.stamp.controllers

import com.example.stamp.mappers.OrderMapper
import com.example.stamp.services.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/orders")
class OrdersV1Controller(
    private val orderService: OrderService,
    private val orderMapper: OrderMapper,
) {
    @PostMapping
    fun requestOrder() =
        ResponseEntity.accepted().body(
            orderMapper.toResponse(
                orderService.requestOrder(),
            ),
        )
}
