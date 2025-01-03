package com.example.stamp.controllers

import com.example.stamp.controllers.requests.StampCodeReportPutV1Request
import com.example.stamp.controllers.requests.StampCodeReportV1Request
import com.example.stamp.controllers.responses.StampCodeReportV1Response
import com.example.stamp.mappers.ReportMapper
import com.example.stamp.services.ReportService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/reports")
class ReportsV1Controller(
    private val reportService: ReportService,
    private val reportMapper: ReportMapper,
) {
    @PostMapping("/stamp-code")
    fun postStampCodeReport(
        @RequestBody stampCodeV1Request: StampCodeReportV1Request,
    ): ResponseEntity<StampCodeReportV1Response> {
        val stampCodeReport = reportService.saveStampCodeReport(stampCodeV1Request)
        return ResponseEntity.accepted().body(
            reportMapper.toResponse(stampCodeReport),
        )
    }

    @PutMapping("/stamp-code/{reportId}")
    fun putStampCodeReport(
        @PathVariable("reportId") reportId: Long,
        @RequestBody reportRequest: StampCodeReportPutV1Request,
    ): ResponseEntity<StampCodeReportV1Response> {
        return ResponseEntity.ok(
            reportMapper.toResponse(
                reportService.putReport(reportId, reportRequest),
            ),
        )
    }
}
