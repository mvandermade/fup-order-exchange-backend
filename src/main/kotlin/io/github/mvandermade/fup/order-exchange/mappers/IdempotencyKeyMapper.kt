package io.github.mvandermade.fup.`order-exchange`.mappers

import com.example.stamp.dtos.OrderIdempotencyKeyDTO
import com.example.stamp.dtos.StampReportIdempotencyKeyDTO
import com.example.stamp.entities.OrderIdempotencyKeyEntity
import com.example.stamp.entities.StampReportIdempotencyKeyEntity
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
