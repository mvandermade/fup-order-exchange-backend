package com.example.stamp.repositories

import com.example.stamp.entities.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun getReferenceFirstByOrderConfirmedIsTrueAndStampIsNullOrderByCreatedAtAsc(): Order?
}
