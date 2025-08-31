package io.github.mvandermade.fup.order.exchange.exceptions

import org.springframework.http.HttpStatus

class OrderNotFoundV1Exception(
    origin: Long,
) : ResponseV1Exception(
        "Order not found",
        HttpStatus.NOT_FOUND,
        "orderId",
        origin.toString(),
        ErrorCode.ORDER_NOT_FOUND,
    )
