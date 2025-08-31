package io.github.mvandermade.fup.`order-exchange`.daemons

import com.example.stamp.entities.OrderStampEntity
import com.example.stamp.entities.StampEntity
import com.example.stamp.entities.StampReportEntity
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampReportRepository
import com.example.stamp.repositories.StampRepository
import com.example.stamp.testutils.buildPostgresContainer
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class StampReportDaemonTest(
    @param:Autowired private val stampReportDaemon: StampReportDaemon,
    @param:Autowired private val stampRepository: StampRepository,
    @param:Autowired private val orderStampRepository: OrderStampRepository,
    @param:Autowired private val orderRepository: OrderRepository,
    @param:Autowired private val stampReportRepository: StampReportRepository,
) {
    @BeforeEach
    fun setUp() {
        // First the FK relations gone...
        stampReportRepository.deleteAll()
        orderStampRepository.deleteAll()

        // Then the entities:
        orderRepository.deleteAll()
        stampRepository.deleteAll()
    }

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
    fun `Stamp has no order entity attached expect mark deleted`() {
        val stamp = stampRepository.save(minRandom<StampEntity>().apply { code = "ABC" })

        val report =
            stampReportRepository.save(
                StampReportEntity(
                    code = stamp.code,
                ),
            )

        stampReportDaemon.findStampReportsAndDelete()

        val reportAfter = stampReportRepository.findByIdOrNull(report.id)
        assertThat(reportAfter?.deletionIsDone).isTrue()
    }

    @Test
    fun `Check if loop continues after not found`() {
        val stamp = stampRepository.save(minRandom<StampEntity>().apply { code = "ABC" })

        val report =
            stampReportRepository.save(
                StampReportEntity(
                    code = stamp.code,
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
                ),
            )

        stampReportDaemon.findStampReportsAndDelete()

        val reportAfter = stampReportRepository.findByIdOrNull(report.id)
        assertThat(reportAfter?.deletionIsDone).isTrue()

        val reportAfter2 = stampReportRepository.findByIdOrNull(report2.id)
        assertThat(reportAfter2?.deletionIsDone).isTrue()

        assertThat(
            orderStampRepository.findByIdOrNull(orderStamp2.id),
        ).isNull()
    }

    companion object {
        @Container
        @ServiceConnection
        @Suppress("unused")
        val postgresContainer = buildPostgresContainer()
    }
}
