package io.github.mvandermade.fup.order.exchange.repositories

import io.github.mvandermade.fup.order.exchange.entities.OrderIdempotencyKeyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderIdempotencyKeyRepository : JpaRepository<OrderIdempotencyKeyEntity, Long> {
    fun findByUserKey(key: String): OrderIdempotencyKeyEntity?
}
