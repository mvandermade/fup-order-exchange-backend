package com.example.stamp.repositories

import com.example.stamp.annotations.SpringBootTestWithCleanup
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTestWithCleanup
class StampRepositoryTest(
    @Autowired val stampRepository: StampRepository,
) {
    @Test
    fun `Should fetch a stamp without an order`() {
        val stamp =
            stampRepository.save(
                minRandom(),
            )

        assertNotNull(stampRepository.findFirstByOrderStampEntityIsNull())
        assertThat(stampRepository.findFirstByOrderStampEntityIsNull()?.id).isEqualTo(stamp.id)
    }
}
