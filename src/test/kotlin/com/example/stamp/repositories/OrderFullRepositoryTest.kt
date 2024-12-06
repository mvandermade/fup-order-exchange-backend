package com.example.stamp.repositories

import com.example.stamp.entities.Stamp
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest(
//    classes = [OrderFullRepository::class, StampRepository::class, OrderRepository::class],
)
class OrderFullRepositoryTest(
    @Autowired private val orderFullRepository: OrderFullRepository,
    @Autowired private val stampRepository: StampRepository,
    @Autowired private val orderRepository: OrderRepository,
) {
    @Test
    fun `EntityGraph is not working`() {
        // This test is here to show that the entitygraph annotation not configured right atm.
        val order = orderFullRepository.save(minRandom())
        val stamp =
            stampRepository.save(
                minRandom<Stamp>().apply {
                    this.order = order
                },
            )
        order.stamp = stamp
        orderRepository.save(order)

        val fetchedOrder = orderFullRepository.findByIdOrNull(order.id)

        assertThat(fetchedOrder?.stamp?.code).isEqualTo(stamp.code)
    }
}
