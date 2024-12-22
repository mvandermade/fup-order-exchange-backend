package com.example.stamp.repositories

import com.example.stamp.entities.StampReportEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StampReportRepository : JpaRepository<StampReportEntity, Long>
