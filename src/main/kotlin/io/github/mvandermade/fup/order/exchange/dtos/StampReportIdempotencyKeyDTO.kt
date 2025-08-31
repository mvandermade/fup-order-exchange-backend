package io.github.mvandermade.fup.order.exchange.dtos

data class StampReportIdempotencyKeyDTO(
    val id: Long,
    val stampReportId: Long,
)
