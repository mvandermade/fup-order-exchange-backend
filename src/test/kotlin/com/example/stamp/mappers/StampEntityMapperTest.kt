package com.example.stamp.mappers

import com.example.stamp.entities.StampEntity
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StampEntityMapperTest {
    private val stampMapper = StampMapper()

    @Test
    fun `map response`() {
        val stampEntity = minRandom<StampEntity>()
        val response = stampMapper.toResponse(stampEntity)

        assertThat(response.code).isEqualTo(stampEntity.code)
    }
}
