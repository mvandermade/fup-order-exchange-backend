package io.github.mvandermade.fup.`order-exchange`.dtos

import java.time.OffsetDateTime

data class OrderDTO(
    val id: Long,
    val createdAt: OffsetDateTime?,
    val stamp: StampDTO?,
)
