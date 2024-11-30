package com.example.stamp.repository

import com.example.stamp.entity.Order
import com.example.stamp.entity.Stamp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StampRepository : JpaRepository<Stamp, Long> {
    fun getFirstByOrderIdIsNull(): Stamp?

    fun findByOrder(order: Order): Stamp?
}
