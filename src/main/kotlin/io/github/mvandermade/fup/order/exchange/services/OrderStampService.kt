package io.github.mvandermade.fup.order.exchange.services

import io.github.mvandermade.fup.order.exchange.dtos.StampDTO
import io.github.mvandermade.fup.order.exchange.entities.OrderEntity
import io.github.mvandermade.fup.order.exchange.entities.OrderStampEntity
import io.github.mvandermade.fup.order.exchange.entities.StampEntity
import io.github.mvandermade.fup.order.exchange.exceptions.OrderNotFoundV1Exception
import io.github.mvandermade.fup.order.exchange.exceptions.WaitingForStampV1Exception
import io.github.mvandermade.fup.order.exchange.mappers.StampMapper
import io.github.mvandermade.fup.order.exchange.repositories.OrderRepository
import io.github.mvandermade.fup.order.exchange.repositories.OrderStampRepository
import io.github.mvandermade.fup.order.exchange.repositories.StampRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderStampService(
    private val stampRepository: StampRepository,
    private val orderRepository: OrderRepository,
    private val orderStampRepository: OrderStampRepository,
    private val stampMapper: StampMapper,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun attachStampsToOrderId(orderId: Long): StampDTO {
        val orderEntity =
            orderRepository.findByIdOrNull(orderId)
                ?: throw OrderNotFoundV1Exception(orderId)
        logger.info("Attach stamp to order: ${orderEntity.id}")

        val stamp = stampRepository.findFirstByOrderStampEntityIsNull()

        if (stamp == null) {
            logger.warn("Attach failed, no stamps left in the database for user")
            throw WaitingForStampV1Exception(orderEntity.id)
        }

        attemptToLink(orderEntity, stamp)

        return stampMapper.toDTO(stamp)
    }

    fun attachStampsToEarliestCreatedAt() {
        val order =
            orderRepository.findFirstByOrderStampEntityIsNullOrderByCreatedAtAsc()
                ?: return
        attachStampsToOrderId(order.id)
    }

    fun attemptToLink(
        orderEntity: OrderEntity,
        stampEntity: StampEntity,
    ) {
        orderStampRepository.save(
            OrderStampEntity(orderEntity, stampEntity),
        )
        logger.info("Stamp attached!")
    }
}
