package com.example.stamp.mappers

import com.example.stamp.controllers.responses.StampResponse
import com.example.stamp.entities.StampEntity
import com.example.stamp.models.Stamp
import org.springframework.stereotype.Component

@Component
class StampMapper {
    fun toResponse(stamp: Stamp): StampResponse {
        return StampResponse(stamp.code)
    }

    fun toStamp(stampEntity: StampEntity): Stamp {
        return Stamp(stampEntity.code)
    }
}
