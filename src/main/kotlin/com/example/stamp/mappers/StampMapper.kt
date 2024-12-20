package com.example.stamp.mappers

import com.example.stamp.controllers.responses.StampV1Response
import com.example.stamp.datatransferobjects.StampDTO
import com.example.stamp.entities.StampEntity
import org.springframework.stereotype.Component

@Component
class StampMapper {
    fun toResponse(stampDTO: StampDTO): StampV1Response {
        return StampV1Response(stampDTO.code)
    }

    fun toDTO(stampEntity: StampEntity): StampDTO {
        return StampDTO(stampEntity.code)
    }
}
