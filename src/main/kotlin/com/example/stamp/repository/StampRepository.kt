package com.example.stamp.repository

import com.example.stamp.entity.StampEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StampRepository : JpaRepository<StampEntity, Long> {
    fun getFirstByOrderIdIsNull(): Optional<StampEntity>

    fun getByOrderIdEquals(orderId: String): Optional<StampEntity>
}
