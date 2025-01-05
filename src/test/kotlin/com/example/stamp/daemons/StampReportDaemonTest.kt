package com.example.stamp.daemons

import com.example.stamp.annotations.SpringBootTestWithCleanup
import com.example.stamp.entities.OrderStampEntity
import com.example.stamp.entities.StampEntity
import com.example.stamp.entities.StampReportEntity
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampReportRepository
import com.example.stamp.repositories.StampRepository
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.time.OffsetDateTime

@SpringBootTestWithCleanup
class StampReportDaemonTest(
    @Autowired private val stampReportDaemon: StampReportDaemon,
    @Autowired private val stampRepository: StampRepository,
    @Autowired private val orderStampRepository: OrderStampRepository,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val stampReportRepository: StampReportRepository,
) {
    @Test
    fun `Should delete orderStamp in database`() {
        val order = orderRepository.save(minRandom())
        val stamp = stampRepository.save(minRandom<StampEntity>().apply { code = "ABC" })

        val orderStamp =
            orderStampRepository.save(
                OrderStampEntity(order, stamp),
            )

        val report =
            stampReportRepository.save(
                StampReportEntity(
                    code = stamp.code,
                    reportIsConfirmedAt = OffsetDateTime.now(),
                    reportIsConfirmed = true,
                ),
            )

        stampReportDaemon.findStampReportsAndDelete()

        val reportAfter = stampReportRepository.findByIdOrNull(report.id)
        assertThat(reportAfter?.deletionIsDone).isTrue()

        assertThat(
            orderStampRepository.findByIdOrNull(orderStamp.id),
        ).isNull()
    }

    @Test
    fun `Date of orderStamp is bigger than the report expect ignore flag`() {
        val order = orderRepository.save(minRandom())
        val stamp = stampRepository.save(minRandom<StampEntity>().apply { code = "ABC" })

        val orderStamp =
            orderStampRepository.save(
                OrderStampEntity(order, stamp),
            )

        val report =
            stampReportRepository.save(
                StampReportEntity(
                    code = stamp.code,
                    reportIsConfirmedAt = OffsetDateTime.now().minusDays(1),
                    reportIsConfirmed = true,
                ),
            )

        stampReportDaemon.findStampReportsAndDelete()

        val reportAfter = stampReportRepository.findByIdOrNull(report.id)
        assertThat(reportAfter?.deletionIsDone).isFalse()
        assertThat(reportAfter?.comparisonIsError).isTrue()

        assertThat(
            orderStampRepository.findByIdOrNull(orderStamp.id),
        ).isNotNull()
    }

    @Test
    fun `Report is not confirmed expect nothing happens`() {
        val order = orderRepository.save(minRandom())
        val stamp = stampRepository.save(minRandom<StampEntity>().apply { code = "ABC" })

        val orderStamp =
            orderStampRepository.save(
                OrderStampEntity(order, stamp),
            )

        val report =
            stampReportRepository.save(
                StampReportEntity(
                    code = stamp.code,
                    reportIsConfirmedAt = OffsetDateTime.now(),
                    reportIsConfirmed = false,
                ),
            )

        stampReportDaemon.findStampReportsAndDelete()

        val reportAfter = stampReportRepository.findByIdOrNull(report.id)
        assertThat(reportAfter?.deletionIsDone).isFalse()

        assertThat(
            orderStampRepository.findByIdOrNull(orderStamp.id),
        ).isNotNull()
    }

    @Test
    fun `Stamp has no order entity attached expect mark deleted`() {
        val stamp = stampRepository.save(minRandom<StampEntity>().apply { code = "ABC" })

        val report =
            stampReportRepository.save(
                StampReportEntity(
                    code = stamp.code,
                    reportIsConfirmedAt = OffsetDateTime.now(),
                    reportIsConfirmed = true,
                ),
            )

        stampReportDaemon.findStampReportsAndDelete()

        val reportAfter = stampReportRepository.findByIdOrNull(report.id)
        assertThat(reportAfter?.deletionIsDone).isTrue()
    }

    @Test
    fun `Error the timestamp is null expect comparison is error`() {
        val order = orderRepository.save(minRandom())
        val stamp = stampRepository.save(minRandom<StampEntity>().apply { code = "ABC" })

        val orderStamp =
            orderStampRepository.save(
                OrderStampEntity(order, stamp),
            )

        val report =
            stampReportRepository.save(
                StampReportEntity(
                    code = stamp.code,
                    reportIsConfirmedAt = null,
                    reportIsConfirmed = true,
                ),
            )

        stampReportDaemon.findStampReportsAndDelete()

        val reportAfter = stampReportRepository.findByIdOrNull(report.id)
        assertThat(reportAfter?.deletionIsDone).isFalse()
        assertThat(reportAfter?.comparisonIsError).isTrue()

        assertThat(
            orderStampRepository.findByIdOrNull(orderStamp.id),
        ).isNotNull()
    }

    @Test
    fun `Check if loop continues after not found`() {
        val stamp = stampRepository.save(minRandom<StampEntity>().apply { code = "ABC" })

        val report =
            stampReportRepository.save(
                StampReportEntity(
                    code = stamp.code,
                    reportIsConfirmedAt = OffsetDateTime.now(),
                    reportIsConfirmed = false,
                ),
            )

        val order2 = orderRepository.save(minRandom())
        val stamp2 = stampRepository.save(minRandom<StampEntity>().apply { code = "DEF" })

        val orderStamp2 =
            orderStampRepository.save(
                OrderStampEntity(order2, stamp2),
            )

        val report2 =
            stampReportRepository.save(
                StampReportEntity(
                    code = stamp2.code,
                    reportIsConfirmedAt = OffsetDateTime.now(),
                    reportIsConfirmed = true,
                ),
            )

        stampReportDaemon.findStampReportsAndDelete()

        val reportAfter = stampReportRepository.findByIdOrNull(report.id)
        assertThat(reportAfter?.deletionIsDone).isFalse()

        val reportAfter2 = stampReportRepository.findByIdOrNull(report2.id)
        assertThat(reportAfter2?.deletionIsDone).isTrue()

        assertThat(
            orderStampRepository.findByIdOrNull(orderStamp2.id),
        ).isNull()
    }
}
