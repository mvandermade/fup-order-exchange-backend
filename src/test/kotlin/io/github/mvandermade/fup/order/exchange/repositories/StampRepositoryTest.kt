package io.github.mvandermade.fup.order.exchange.repositories

import com.example.stamp.testutils.buildPostgresContainer
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class StampRepositoryTest(
    @param:Autowired val stampRepository: StampRepository,
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

    companion object {
        @Container
        @ServiceConnection
        val postgresContainer = buildPostgresContainer()
    }
}
