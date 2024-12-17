package com.example.stamp.mappers

import com.example.stamp.entities.StampEntity
import com.example.stamp.models.Stamp
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StampEntityMapperTest {
    private val stampMapper = StampMapper()

    @Test
    fun `map stamp to response`() {
        val stamp = minRandom<Stamp>()
        val response = stampMapper.toResponse(stamp)

        assertThat(response.code).isEqualTo(stamp.code)
    }

    @Test
    fun `Map stampEntity to stamp`() {
        val stampEntity = minRandom<StampEntity>()
        val stamp = stampMapper.toStamp(stampEntity)

        assertThat(stamp.code).isEqualTo(stampEntity.code)
    }
}
