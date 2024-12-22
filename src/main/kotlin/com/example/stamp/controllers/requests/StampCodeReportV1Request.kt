package com.example.stamp.controllers.requests

import java.time.OffsetDateTime

data class StampCodeReportV1Request(
    val code: String,
    val offsetDateTime: OffsetDateTime?,
    val reachedDestination: Boolean?,
    val comment: String?,
)
