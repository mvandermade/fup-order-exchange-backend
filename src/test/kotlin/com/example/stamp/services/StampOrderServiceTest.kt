package com.example.stamp.services

import com.example.stamp.entities.Stamp
import com.example.stamp.repositories.OrderRepository
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
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@ExtendWith(OutputCaptureExtension::class)
class StampOrderServiceTest(
    @Autowired private val stampOrderService: StampOrderService,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val stampRepository: StampRepository,
) {
    @BeforeEach
    fun setUp() {
        stampRepository.deleteAll()
        orderRepository.deleteAll()
    }

    @Test
    fun `Attempt to link can only be done once`(output: CapturedOutput) {
        val order1 = orderRepository.save(minRandom())
        val order2 = orderRepository.save(minRandom())

        val stamp1 =
            stampRepository.save(
                minRandom<Stamp>().apply {
                    this.code = "ABCD"
                    this.order = null
                },
            )

        stampOrderService.attemptToLink(order1, stamp1)

        assertThat(output.out.lines())
            .filteredOn{ element -> element.contains("Stamp attached!")}
            .hasSize(1)

        assertThrows<ObjectOptimisticLockingFailureException> {
            stampOrderService.attemptToLink(order2, stamp1)
        }
    }
}
