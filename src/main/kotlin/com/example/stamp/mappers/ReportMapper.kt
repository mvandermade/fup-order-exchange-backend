package com.example.stamp.mappers

import com.example.stamp.controllers.responses.StampReportV1Response
import com.example.stamp.dtos.StampReportDTO
import com.example.stamp.entities.StampReportEntity
import org.springframework.stereotype.Component

@Component
class ReportMapper {
    fun toResponse(stampReport: StampReportDTO): StampReportV1Response {
        return StampReportV1Response(
            id = stampReport.id,
        )
    }

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
