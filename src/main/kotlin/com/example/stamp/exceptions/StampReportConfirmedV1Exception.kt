package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class StampReportConfirmedV1Exception(origin: Long) : ResponseV1Exception(
    "Stamp report is confirmed, no modifications possible anymore.",
    HttpStatus.NOT_ACCEPTABLE,
    "reportId",
    origin.toString(),
)
