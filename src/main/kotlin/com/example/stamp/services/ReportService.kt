package com.example.stamp.services

import com.example.stamp.controllers.requests.StampCodeReportV1Request
import com.example.stamp.dtos.StampCodeReportDTO
import com.example.stamp.entities.StampReportEntity
import com.example.stamp.mappers.ReportMapper
import com.example.stamp.repositories.StampReportRepository
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val stampReportRepository: StampReportRepository,
    private val reportMapper: ReportMapper,
) {
    fun postStampCodeReport(stampCodeV1Request: StampCodeReportV1Request): StampCodeReportDTO {
        val stampReportEntity =
            stampReportRepository.save(
                StampReportEntity(
                    code = stampCodeV1Request.code,
                    createdAtObserver = stampCodeV1Request.offsetDateTime,
                    reachedDestination = stampCodeV1Request.reachedDestination,
                    comment = stampCodeV1Request.comment,
                ),
            )

        return reportMapper.toDTO(stampReportEntity)
    }
}
