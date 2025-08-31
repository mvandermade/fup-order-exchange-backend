package io.github.mvandermade.fup.order.exchange.repositories

import io.github.mvandermade.fup.order.exchange.entities.StampReportEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface StampReportRepository : JpaRepository<StampReportEntity, Long> {
    fun findByDeletionIsDoneIsFalseAndComparisonIsErrorIsFalse(pageable: Pageable): Page<StampReportEntity>
}
