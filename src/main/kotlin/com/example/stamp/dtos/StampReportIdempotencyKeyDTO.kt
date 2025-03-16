package com.example.stamp.dtos

data class StampReportIdempotencyKeyDTO(
    val id: Long,
    val stampReportId: Long,
)
