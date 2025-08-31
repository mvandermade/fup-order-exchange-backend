package io.github.mvandermade.fup.order.exchange.repositories

import com.example.stamp.entities.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderEntity, Long> {
    fun findFirstByOrderStampEntityIsNullOrderByCreatedAtAsc(): OrderEntity?
}
