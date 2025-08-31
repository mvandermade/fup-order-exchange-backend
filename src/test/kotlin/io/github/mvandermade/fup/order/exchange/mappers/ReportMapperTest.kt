package io.github.mvandermade.fup.order.exchange.mappers

import io.github.mvandermade.fup.order.exchange.entities.StampReportEntity
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class ReportMapperTest {
    val reportMapper = ReportMapper()

    @Nested
    inner class StampReport {
        @Test
        fun `DTO to response`() {
            val stampReportEntity =
                minRandom<StampReportEntity>().apply {
                    createdAt = OffsetDateTime.now()
                }
            val stampReportDTO = reportMapper.toDTO(stampReportEntity)
            val response = reportMapper.toResponse(stampReportDTO)

            assertThat(response.id).isEqualTo(stampReportDTO.id)
        }

        @Test
        fun `Entity to DTO`() {
            val observerTime = OffsetDateTime.now()
            val serverTime = OffsetDateTime.now()

            val stampReportEntity =
                minRandom<StampReportEntity>().apply {
                    createdAt = serverTime
                    createdAtObserver = observerTime
                    reachedDestination = true
                    comment = "XYZ"
                }

            val stampReportDTO = reportMapper.toDTO(stampReportEntity)

            assertThat(stampReportDTO.id).isEqualTo(stampReportEntity.id)
            assertThat(stampReportDTO.createdAtObserver).isEqualTo(observerTime)
            assertThat(stampReportDTO.createdAtServer).isEqualTo(serverTime)
            assertThat(stampReportDTO.comment).isEqualTo(stampReportEntity.comment)
            assertThat(stampReportDTO.reachedDestination).isEqualTo(stampReportEntity.reachedDestination)
            assertThat(stampReportDTO.code).isEqualTo(stampReportEntity.code)
        }
    }
}
