package io.github.mvandermade.fup.order.exchange.mappers

import io.github.mvandermade.fup.order.exchange.dtos.StampDTO
import io.github.mvandermade.fup.order.exchange.entities.StampEntity
import org.springframework.stereotype.Component

@Component
class StampMapper {
    fun toDTO(stampEntity: StampEntity): StampDTO = StampDTO(stampEntity.code)
}
