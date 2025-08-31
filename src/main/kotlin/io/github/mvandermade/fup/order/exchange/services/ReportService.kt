package io.github.mvandermade.fup.order.exchange.services

import io.github.mvandermade.fup.order.exchange.controllers.requests.StampReportV1Request
import io.github.mvandermade.fup.order.exchange.dtos.StampReportDTO
import io.github.mvandermade.fup.order.exchange.entities.StampReportEntity
import io.github.mvandermade.fup.order.exchange.exceptions.StampReportNotFoundV1Exception
import io.github.mvandermade.fup.order.exchange.mappers.ReportMapper
import io.github.mvandermade.fup.order.exchange.providers.TransactionProvider
import io.github.mvandermade.fup.order.exchange.repositories.StampReportRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReportService(
    private val stampReportRepository: StampReportRepository,
    private val reportMapper: ReportMapper,
    private val stampReportIdempotencyKeyService: StampReportIdempotencyKeyService,
    private val transactionProvider: TransactionProvider,
) {
    @Transactional(rollbackFor = [Exception::class])
    fun postStampReport(
        stampReportV1Request: StampReportV1Request,
        idempotencyKey: String,
    ): StampReportDTO {
        // Need to put it in a val otherwise tests will fail
        val committedEntity =
            transactionProvider.newReadWrite {
                val stampReportEntity =
                    stampReportRepository.save(
                        StampReportEntity(
                            code = stampReportV1Request.code,
                            createdAtObserver = stampReportV1Request.offsetDateTime,
                            reachedDestination = stampReportV1Request.reachedDestination,
                            comment = stampReportV1Request.comment,
                        ),
                    )
                stampReportIdempotencyKeyService.saveIdempotencyKey(idempotencyKey, stampReportEntity.id)
                stampReportEntity
            }

        return reportMapper.toDTO(committedEntity)
    }

    fun getStampReport(stampReportId: Long): StampReportDTO {
        val entity =
            stampReportRepository.findByIdOrNull(stampReportId)
                ?: throw StampReportNotFoundV1Exception(stampReportId)
        return reportMapper.toDTO(entity)
    }
}
