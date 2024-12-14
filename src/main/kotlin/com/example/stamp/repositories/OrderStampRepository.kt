package com.example.stamp.repositories

import com.example.stamp.entities.OrderStamp
import org.springframework.data.jpa.repository.JpaRepository

interface OrderStampRepository : JpaRepository<OrderStamp, Long>
