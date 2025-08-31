package io.github.mvandermade.fup.order.exchange.dtos

data class OrderIdempotencyKeyDTO(
    val id: Long,
    val orderId: Long,
)
