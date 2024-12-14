package com.example.stamp.repositories

import com.example.stamp.entities.OrderStamp
import com.example.stamp.entities.Stamp
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class OrderFullRepositoryTest(
    @Autowired private val orderFullRepository: OrderFullRepository,
    @Autowired private val stampRepository: StampRepository,
) {
    @Autowired
    private lateinit var orderStampRepository: OrderStampRepository

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
    @Disabled
    fun `EntityGraph is working`() {
        val order = orderFullRepository.save(minRandom())
        val stamp =
            stampRepository.save(
                minRandom<Stamp>().apply {
                    this.code = "ABCD"
                },
            )
        orderStampRepository.saveAndFlush(
            OrderStamp(order, stamp),
        )

        val fetchedOrder = orderFullRepository.findByIdOrNull(order.id)

        assertThat(fetchedOrder).isNotNull()
        assertThat(fetchedOrder?.orderStamp).isNotNull()
        assertThat(fetchedOrder?.orderStamp?.stamp).isNotNull()
        assertThat(fetchedOrder?.orderStamp?.stamp?.id).isNotNull()
        assertThat(fetchedOrder?.orderStamp?.stamp?.code).isEqualTo("ABCD")
    }

    @Test
    fun `EntityGraph isn't working`() {
        val order = orderFullRepository.save(minRandom())
        val stamp =
            stampRepository.save(
                minRandom<Stamp>().apply {
                    this.code = "ABCD"
                },
            )
        orderStampRepository.saveAndFlush(
            OrderStamp(order, stamp),
        )

        val fetchedOrder = orderFullRepository.findByIdOrNull(order.id)

        assertThat(fetchedOrder).isNotNull()
        assertThat(fetchedOrder?.orderStamp).isNotNull()
        assertThat(fetchedOrder?.orderStamp?.stamp).isNotNull()
        assertThat(fetchedOrder?.orderStamp?.stamp?.id).isNotNull()
        val foundStamp = fetchedOrder?.orderStamp?.stamp?.id?.let { stampRepository.findByIdOrNull(it) }

        assertThat(foundStamp?.code).isEqualTo("ABCD")
    }
}
