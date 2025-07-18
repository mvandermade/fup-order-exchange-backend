package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class WaitingForStampV1Exception(
    origin: Long,
) : ResponseV1Exception(
        "Stamp collection is in progress",
        HttpStatus.TOO_EARLY,
        "orderId",
        origin.toString(),
        ErrorCode.STAMP_COLLECTION_IN_PROGRESS,
    )
