package com.example.stamp.services

import com.example.stamp.entities.Stamp
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.StampRepository
import nl.wykorijnsburger.kminrandom.minRandom
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class StampOrderServiceTest(
    @Autowired private val stampOrderService: StampOrderService,
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val stampRepository: StampRepository,
) {
    @Test
    fun `Attempt to link can only be done once`() {
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
        assertThrows<ObjectOptimisticLockingFailureException> {
            stampOrderService.attemptToLink(order2, stamp1)
        }
    }
}
