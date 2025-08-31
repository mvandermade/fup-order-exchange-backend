package io.github.mvandermade.fup.order.exchange.services

import com.example.stamp.dtos.StampReportIdempotencyKeyDTO
import com.example.stamp.entities.StampReportIdempotencyKeyEntity
import com.example.stamp.mappers.IdempotencyKeyMapper
import com.example.stamp.repositories.StampReportIdempotencyKeyRepository
import com.example.stamp.repositories.StampReportRepository
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
