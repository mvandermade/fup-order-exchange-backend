package com.example.stamp.repositories

import com.example.stamp.entities.OrderIdempotencyKeyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderIdempotencyKeyRepository : JpaRepository<OrderIdempotencyKeyEntity, Long> {
    fun findByUserKey(key: String): OrderIdempotencyKeyEntity?
}
