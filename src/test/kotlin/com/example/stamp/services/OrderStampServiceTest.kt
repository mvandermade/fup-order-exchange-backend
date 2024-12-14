package com.example.stamp.services

import com.example.stamp.entities.Stamp
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampRepository
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.dao.DataIntegrityViolationException

@SpringBootTest
@ExtendWith(OutputCaptureExtension::class)
class OrderStampServiceTest(
    @Autowired private val orderStampService: OrderStampService,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val stampRepository: StampRepository,
    @Autowired private val orderStampRepository: OrderStampRepository,
) {
    @BeforeEach
    fun setUp(
        @Autowired orderRepository: OrderRepository,
        @Autowired orderStampRepository: OrderStampRepository,
        @Autowired stampRepository: StampRepository,
    ) {
        orderStampRepository.deleteAllInBatch()
        stampRepository.deleteAllInBatch()
        orderRepository.deleteAllInBatch()
    }

    @Test
    fun `Link results in logging`(output: CapturedOutput) {
        val order1 = orderRepository.save(minRandom())

        val stamp1 =
            stampRepository.save(
                minRandom<Stamp>().apply {
                    this.code = "ABCD"
                },
            )

        orderStampService.attemptToLink(order1, stamp1)

        assertThat(output.out.lines())
            .filteredOn { element -> element.contains("Stamp attached!") }
            .hasSize(1)
    }

    @Test
    fun `Attempt to link can only be done once due to unique constraint`() {
        val order1 = orderRepository.save(minRandom())
        val order2 = orderRepository.save(minRandom())

        val stamp1 =
            stampRepository.save(
                minRandom<Stamp>().apply {
                    this.code = "ABCD"
                },
            )

        orderStampService.attemptToLink(order1, stamp1)

        assertThrows<DataIntegrityViolationException> {
            orderStampService.attemptToLink(order2, stamp1)
        }
    }
}
