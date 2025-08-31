package io.github.mvandermade.fup.order.exchange.mappers

import io.github.mvandermade.fup.order.exchange.dtos.OrderIdempotencyKeyDTO
import io.github.mvandermade.fup.order.exchange.dtos.StampReportIdempotencyKeyDTO
import io.github.mvandermade.fup.order.exchange.entities.OrderIdempotencyKeyEntity
import io.github.mvandermade.fup.order.exchange.entities.StampReportIdempotencyKeyEntity
import org.springframework.stereotype.Component

@Component
class IdempotencyKeyMapper {
    fun toDTO(entity: OrderIdempotencyKeyEntity): OrderIdempotencyKeyDTO =
        OrderIdempotencyKeyDTO(
            id = entity.id,
            orderId = entity.order.id,
        )

    fun toDTO(entity: StampReportIdempotencyKeyEntity): StampReportIdempotencyKeyDTO =
        StampReportIdempotencyKeyDTO(
            id = entity.id,
            stampReportId = entity.stampReport.id,
        )
}
