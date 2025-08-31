package io.github.mvandermade.fup.order.exchange.mappers

import io.github.mvandermade.fup.order.exchange.controllers.responses.StampReportV1Response
import io.github.mvandermade.fup.order.exchange.dtos.StampReportDTO
import io.github.mvandermade.fup.order.exchange.entities.StampReportEntity
import org.springframework.stereotype.Component

@Component
class ReportMapper {
    fun toResponse(stampReport: StampReportDTO): StampReportV1Response =
        StampReportV1Response(
            id = stampReport.id,
        )

    fun toDTO(stampReportEntity: StampReportEntity): StampReportDTO {
        val createdAtServer =
            stampReportEntity.createdAt
                ?: throw IllegalArgumentException("CreatedAt is missing")

        return StampReportDTO(
            id = stampReportEntity.id,
            code = stampReportEntity.code,
            createdAtServer = createdAtServer,
            createdAtObserver = stampReportEntity.createdAtObserver,
            reachedDestination = stampReportEntity.reachedDestination,
            comment = stampReportEntity.comment,
        )
    }
}
