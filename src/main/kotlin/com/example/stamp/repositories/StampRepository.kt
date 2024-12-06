package com.example.stamp.repositories

import com.example.stamp.entities.Order
import com.example.stamp.entities.Stamp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StampRepository : JpaRepository<Stamp, Long> {
    fun getFirstByOrderIdIsNull(): Stamp?

    fun findByOrder(order: Order): Stamp?
}
