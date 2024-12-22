package com.example.stamp.mappers

import com.example.stamp.controllers.responses.StampCodeReportV1Response
import com.example.stamp.dtos.StampCodeReportDTO
import com.example.stamp.entities.StampReportEntity
import org.springframework.stereotype.Component

@Component
class ReportMapper {
    fun toResponse(stampCodeReport: StampCodeReportDTO): StampCodeReportV1Response {
        return StampCodeReportV1Response(
            id = stampCodeReport.id,
            reportIsConfirmed = stampCodeReport.reportIsConfirmed,
        )
    }

    fun toDTO(stampReportEntity: StampReportEntity): StampCodeReportDTO {
        val createdAtServer =
            stampReportEntity.createdAt
                ?: throw IllegalArgumentException("CreatedAt is missing")

        return StampCodeReportDTO(
            id = stampReportEntity.id,
            reportIsConfirmed = stampReportEntity.reportIsConfirmed,
            code = stampReportEntity.code,
            createdAtServer = createdAtServer,
            createdAtObserver = stampReportEntity.createdAtObserver,
            reachedDestination = stampReportEntity.reachedDestination,
            comment = stampReportEntity.comment,
        )
    }
}
