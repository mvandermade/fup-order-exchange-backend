package com.example.stamp.repository

import com.example.stamp.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun getFirstByCpConfirmedIsTrueAndStampIsNull(): Order?
}
