package com.example.stamp.repositories

import com.example.stamp.entities.Stamp
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class StampRepositoryTest(
    @Autowired val stampRepository: StampRepository,
) {
    @Test
    fun `Should fetch a stamp without an order`() {
        val stamp =
            stampRepository.save(
                minRandom<Stamp>().apply {
                    this.order = null
                },
            )

        assertNotNull(stampRepository.findFirstByOrderIsNull())
        assertThat(stampRepository.findFirstByOrderIsNull()?.id).isEqualTo(stamp.id)
    }
}
