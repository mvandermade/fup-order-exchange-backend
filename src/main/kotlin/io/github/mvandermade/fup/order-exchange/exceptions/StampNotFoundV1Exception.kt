package io.github.mvandermade.fup.`order-exchange`.exceptions

import org.springframework.http.HttpStatus

class StampNotFoundV1Exception(
    origin: Long,
) : ResponseV1Exception(
        "Stamp not found",
        HttpStatus.NOT_FOUND,
        "orderId",
        origin.toString(),
        ErrorCode.STAMP_NOT_FOUND,
    )
