package io.github.mvandermade.fup.order.exchange.mappers

import com.example.stamp.entities.StampEntity
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StampMapperTest {
    private val stampMapper = StampMapper()

    @Test
    fun `Map stampEntity to stamp DTO`() {
        val stampEntity = minRandom<StampEntity>()
        val stamp = stampMapper.toDTO(stampEntity)

        assertThat(stamp.code).isEqualTo(stampEntity.code)
    }
}
