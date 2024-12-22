package com.example.stamp.controllers

import com.example.stamp.controllers.requests.OrderV1Request
import com.example.stamp.controllers.responses.OrderV1Response
import com.example.stamp.mappers.OrderMapper
import com.example.stamp.services.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/orders")
class OrdersV1Controller(
    private val orderService: OrderService,
    private val orderMapper: OrderMapper,
) {
    @GetMapping("/{orderId}")
    fun getOrder(
        @PathVariable("orderId") orderId: Long,
    ): ResponseEntity<OrderV1Response> {
        val orderDTO = orderService.attemptStampCollection(orderId)
        return ResponseEntity.ok(
            orderMapper.toResponse(orderDTO),
        )
    }

    @PostMapping
    fun postOrder() =
        ResponseEntity.accepted().body(
            orderMapper.toResponse(
                orderService.postOrder(),
            ),
        )

    @PutMapping("/{orderId}")
    fun updateOrder(
        @PathVariable("orderId") orderId: Long,
        @RequestBody orderRequest: OrderV1Request,
    ): ResponseEntity<OrderV1Response> {
        return ResponseEntity.ok(
            orderMapper.toResponse(
                orderService.putOrder(orderId, orderRequest),
            ),
        )
    }
}
