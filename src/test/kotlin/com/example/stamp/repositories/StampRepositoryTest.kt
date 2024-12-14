package com.example.stamp.repositories

import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StampRepositoryTest(
    @Autowired val stampRepository: StampRepository,
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
    fun `Should fetch a stamp without an order`() {
        val stamp =
            stampRepository.save(
                minRandom(),
            )

        assertNotNull(stampRepository.findFirstByOrderStampIsNull())
        assertThat(stampRepository.findFirstByOrderStampIsNull()?.id).isEqualTo(stamp.id)
    }
}
