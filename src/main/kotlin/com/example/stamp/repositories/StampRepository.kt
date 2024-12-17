package com.example.stamp.repositories

import com.example.stamp.entities.StampEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StampRepository : JpaRepository<StampEntity, Long> {
    fun findFirstByOrderStampIsNull(): StampEntity?
}
