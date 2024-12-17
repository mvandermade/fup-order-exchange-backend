package com.example.stamp.repositories

import com.example.stamp.entities.OrderStampEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderStampRepository : JpaRepository<OrderStampEntity, Long>
