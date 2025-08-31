package io.github.mvandermade.fup.order.exchange.mappers

import com.example.stamp.dtos.StampDTO
import com.example.stamp.entities.StampEntity
import org.springframework.stereotype.Component

@Component
class StampMapper {
    fun toDTO(stampEntity: StampEntity): StampDTO = StampDTO(stampEntity.code)
}
