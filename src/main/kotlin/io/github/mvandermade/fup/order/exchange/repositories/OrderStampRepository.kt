package io.github.mvandermade.fup.order.exchange.repositories

import io.github.mvandermade.fup.order.exchange.entities.OrderStampEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderStampRepository : JpaRepository<OrderStampEntity, Long>
