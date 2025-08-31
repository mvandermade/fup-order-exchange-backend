package io.github.mvandermade.fup.order.exchange.services

import io.github.mvandermade.fup.order.exchange.dtos.StampReportIdempotencyKeyDTO
import io.github.mvandermade.fup.order.exchange.entities.StampReportIdempotencyKeyEntity
import io.github.mvandermade.fup.order.exchange.mappers.IdempotencyKeyMapper
import io.github.mvandermade.fup.order.exchange.repositories.StampReportIdempotencyKeyRepository
import io.github.mvandermade.fup.order.exchange.repositories.StampReportRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StampReportIdempotencyKeyService(
    private val stampReportIdempotencyKeyRepository: StampReportIdempotencyKeyRepository,
    private val idempotencyKeyMapper: IdempotencyKeyMapper,
    private val stampReportRepository: StampReportRepository,
) {
    fun getIdempotencyKeyDTO(userKey: String): StampReportIdempotencyKeyDTO? {
        val entity = stampReportIdempotencyKeyRepository.findByUserKey(userKey)

        if (entity != null) {
            return idempotencyKeyMapper.toDTO(entity)
        }

        return null
    }

    @Transactional(rollbackFor = [Exception::class])
    fun saveIdempotencyKey(
        userKey: String,
        stampReportId: Long,
    ): StampReportIdempotencyKeyDTO {
        val stampReportEntity = stampReportRepository.getReferenceById(stampReportId)
        return idempotencyKeyMapper.toDTO(
            stampReportIdempotencyKeyRepository.save(
                StampReportIdempotencyKeyEntity(
                    userKey = userKey,
                    stampReport = stampReportEntity,
                ),
            ),
        )
    }
}
