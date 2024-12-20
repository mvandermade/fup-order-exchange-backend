package com.example.stamp.mappers

import com.example.stamp.datatransferobjects.StampDTO
import com.example.stamp.entities.StampEntity
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StampDTOEntityMapperTest {
    private val stampMapper = StampMapper()

    @Test
    fun `map stamp to response`() {
        val stampDTO = minRandom<StampDTO>()
        val response = stampMapper.toResponse(stampDTO)

        assertThat(response.code).isEqualTo(stampDTO.code)
    }

    @Test
    fun `Map stampEntity to stamp`() {
        val stampEntity = minRandom<StampEntity>()
        val stamp = stampMapper.toDTO(stampEntity)

        assertThat(stamp.code).isEqualTo(stampEntity.code)
    }
}
