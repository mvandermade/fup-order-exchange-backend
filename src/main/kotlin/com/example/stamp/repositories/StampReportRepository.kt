package com.example.stamp.repositories

import com.example.stamp.entities.StampReportEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface StampReportRepository : JpaRepository<StampReportEntity, Long> {
    fun findByDeletionIsDoneIsFalseAndReportIsConfirmedIsTrueAndComparisonIsErrorIsFalse(pageable: Pageable): Page<StampReportEntity>
}
