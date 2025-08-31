package io.github.mvandermade.fup.order.exchange.controllers

import com.example.stamp.controllers.requests.StampReportV1Request
import com.example.stamp.controllers.responses.StampReportV1Response
import com.example.stamp.mappers.ReportMapper
import com.example.stamp.services.ReportService
import com.example.stamp.services.StampReportIdempotencyKeyService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/reports")
class ReportsV1Controller(
    private val reportService: ReportService,
    private val reportMapper: ReportMapper,
    private val stampReportIdempotencyKeyService: StampReportIdempotencyKeyService,
) {
    @PostMapping("/stamp-code")
    fun postStampReport(
        @RequestBody stampReportV1Request: StampReportV1Request,
        @RequestHeader(name = "idempotency-key", required = true) idempotencyKeyHeader: String,
    ): ResponseEntity<StampReportV1Response> {
        val idempotencyKey =
            stampReportIdempotencyKeyService.getIdempotencyKeyDTO(idempotencyKeyHeader)
                ?: return ResponseEntity.ok(tryPostStampReport(stampReportV1Request, idempotencyKeyHeader))

        return ResponseEntity.ok(
            reportMapper.toResponse(
                reportService.getStampReport(idempotencyKey.stampReportId),
            ),
        )
    }

    private fun tryPostStampReport(
        stampReportV1Request: StampReportV1Request,
        idempotencyKeyHeader: String,
    ): StampReportV1Response {
        val response =
            try {
                reportMapper.toResponse(
                    reportService.postStampReport(stampReportV1Request, idempotencyKeyHeader),
                )
            } catch (e: DataIntegrityViolationException) {
                // Try one more time to fetch it now...
                val idempotencyKey =
                    stampReportIdempotencyKeyService.getIdempotencyKeyDTO(idempotencyKeyHeader)
                        ?: throw e

                reportMapper.toResponse(
                    reportService.getStampReport(idempotencyKey.stampReportId),
                )
            }

        return response
    }
}
