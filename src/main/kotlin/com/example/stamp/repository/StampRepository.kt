package com.example.stamp.repository

import com.example.stamp.entity.Stamp
import org.springframework.data.jpa.repository.JpaRepository

interface StampRepository : JpaRepository<Stamp, Long> {
    fun getFirstByOrderIdIsNull(): Stamp?
}
