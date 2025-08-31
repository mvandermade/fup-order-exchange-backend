package io.github.mvandermade.fup.`order-exchange`.services

import com.example.stamp.dtos.OrderIdempotencyKeyDTO
import com.example.stamp.entities.OrderIdempotencyKeyEntity
import com.example.stamp.mappers.IdempotencyKeyMapper
import com.example.stamp.repositories.OrderIdempotencyKeyRepository
import com.example.stamp.repositories.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderIdempotencyKeyService(
    private val orderIdempotencyKeyRepository: OrderIdempotencyKeyRepository,
    private val idempotencyKeyMapper: IdempotencyKeyMapper,
    private val orderRepository: OrderRepository,
) {
    fun getIdempotencyKeyDTO(userKey: String): OrderIdempotencyKeyDTO? {
        val entity = orderIdempotencyKeyRepository.findByUserKey(userKey)

        if (entity != null) {
            return idempotencyKeyMapper.toDTO(entity)
        }

        return null
    }

    @Transactional(rollbackFor = [Exception::class])
    fun saveIdempotencyKey(
        userKey: String,
        orderId: Long,
    ): OrderIdempotencyKeyDTO {
        val orderEntity = orderRepository.getReferenceById(orderId)
        return idempotencyKeyMapper.toDTO(
            orderIdempotencyKeyRepository.save(
                OrderIdempotencyKeyEntity(
                    userKey = userKey,
                    order = orderEntity,
                ),
            ),
        )
    }
}
