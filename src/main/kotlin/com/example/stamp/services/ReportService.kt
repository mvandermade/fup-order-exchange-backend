package com.example.stamp.services

import com.example.stamp.controllers.requests.StampReportV1Request
import com.example.stamp.dtos.StampReportDTO
import com.example.stamp.entities.StampReportEntity
import com.example.stamp.exceptions.StampReportNotFoundV1Exception
import com.example.stamp.mappers.ReportMapper
import com.example.stamp.providers.TransactionProvider
import com.example.stamp.repositories.StampReportRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val stampReportRepository: StampReportRepository,
    private val reportMapper: ReportMapper,
    private val stampReportIdempotencyKeyService: StampReportIdempotencyKeyService,
    private val transactionProvider: TransactionProvider,
) {
    fun postStampReport(
        stampReportV1Request: StampReportV1Request,
        idempotencyKey: String,
    ): StampReportDTO {
        val stampReportEntity =
            transactionProvider.newReadWrite {
                val entity =
                    stampReportRepository.save(
                        StampReportEntity(
                            code = stampReportV1Request.code,
                            createdAtObserver = stampReportV1Request.offsetDateTime,
                            reachedDestination = stampReportV1Request.reachedDestination,
                            comment = stampReportV1Request.comment,
                        ),
                    )
                stampReportIdempotencyKeyService.saveIdempotencyKey(idempotencyKey, entity.id)
                entity
            }

        return reportMapper.toDTO(stampReportEntity)
    }

    fun getStampReport(stampReportId: Long): StampReportDTO {
        val entity =
            stampReportRepository.findByIdOrNull(stampReportId)
                ?: throw StampReportNotFoundV1Exception(stampReportId)
        return reportMapper.toDTO(entity)
    }
}
