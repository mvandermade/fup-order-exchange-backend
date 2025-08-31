package io.github.mvandermade.fup.order.exchange.repositories

import io.github.mvandermade.fup.order.exchange.entities.StampEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StampRepository : JpaRepository<StampEntity, Long> {
    fun findFirstByOrderStampEntityIsNull(): StampEntity?

    fun findByCode(code: String): StampEntity?
}
