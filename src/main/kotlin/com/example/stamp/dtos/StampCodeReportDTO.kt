package com.example.stamp.dtos

import java.time.OffsetDateTime

data class StampCodeReportDTO(
    val id: Long,
    val reportIsConfirmed: Boolean,
    val code: String,
    val createdAtServer: OffsetDateTime,
    val createdAtObserver: OffsetDateTime?,
    val reachedDestination: Boolean?,
    val comment: String?,
)
