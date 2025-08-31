package io.github.mvandermade.fup.order.exchange.controllers

import com.example.stamp.controllers.responses.OrderV1Response
import com.example.stamp.mappers.OrderMapper
import com.example.stamp.services.OrderIdempotencyKeyService
import com.example.stamp.services.OrderService
import org.springframework.context.annotation.Description
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/orders")
class OrdersV1Controller(
    private val orderService: OrderService,
    private val orderMapper: OrderMapper,
    private val orderIdempotencyKeyService: OrderIdempotencyKeyService,
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
    @Description("Use an unique idempotency key to prevent duplicate ordering")
    fun postOrder(
        @RequestHeader(name = "idempotency-key", required = true) idempotencyKeyHeader: String,
    ): ResponseEntity<OrderV1Response> {
        val idempotencyKey =
            orderIdempotencyKeyService.getIdempotencyKeyDTO(idempotencyKeyHeader)
                ?: return ResponseEntity.ok(
                    orderMapper.toResponse(
                        orderService.postOrder(idempotencyKeyHeader),
                    ),
                )

        return ResponseEntity.ok(
            orderMapper.toResponse(
                orderService.getOrder(idempotencyKey.orderId),
            ),
        )
    }
}
