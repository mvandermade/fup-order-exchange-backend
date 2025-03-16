package com.example.stamp.repositories

import com.example.stamp.entities.StampReportIdempotencyKeyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StampReportIdempotencyKeyRepository : JpaRepository<StampReportIdempotencyKeyEntity, Long> {
    fun findByUserKey(key: String): StampReportIdempotencyKeyEntity?
}
