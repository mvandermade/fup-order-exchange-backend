package com.example.stamp.exceptions

import org.springframework.http.HttpStatus

class StampReportNotFoundV1Exception(
    origin: Long,
) : ResponseV1Exception(
        "Stamp report not found",
        HttpStatus.NOT_FOUND,
        "reportId",
        origin.toString(),
        ErrorCode.STAMP_CODE_REPORT_NOT_FOUND,
    )
