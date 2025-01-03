package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class StampCodeReportNotFoundV1Exception(origin: Long) : ResponseV1Exception(
    "Stamp code report not found",
    HttpStatus.NOT_FOUND,
    "reportId",
    origin.toString(),
)
