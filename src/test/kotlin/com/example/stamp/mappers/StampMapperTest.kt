package com.example.stamp.mappers

import com.example.stamp.entities.Stamp
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StampMapperTest {
    private val stampMapper = StampMapper()

    @Test
    fun `map response`() {
        val stamp = minRandom<Stamp>()
        val response = stampMapper.toResponse(stamp)

        assertThat(response.code).isEqualTo(stamp.code)
    }
}
