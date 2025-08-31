package io.github.mvandermade.fup.order.exchange.services

import io.github.mvandermade.fup.order.exchange.dtos.OrderDTO
import io.github.mvandermade.fup.order.exchange.entities.OrderEntity
import io.github.mvandermade.fup.order.exchange.exceptions.OrderNotFoundV1Exception
import io.github.mvandermade.fup.order.exchange.mappers.OrderMapper
import io.github.mvandermade.fup.order.exchange.providers.TransactionProvider
import io.github.mvandermade.fup.order.exchange.repositories.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderMapper: OrderMapper,
    private val orderStampService: OrderStampService,
    private val orderIdempotencyKeyService: OrderIdempotencyKeyService,
    private val transactionProvider: TransactionProvider,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun postOrder(idempotentUserKey: String): OrderDTO {
        val committedEntity =
            transactionProvider.newReadWrite {
                val entity =
                    orderRepository.save(
                        OrderEntity(),
                    )
                orderIdempotencyKeyService.saveIdempotencyKey(idempotentUserKey, entity.id)
                entity
            }

        return orderMapper.toDTO(committedEntity, committedEntity.orderStampEntity?.stampEntity)
    }

    fun getOrder(orderId: Long): OrderDTO {
        val entity =
            orderRepository.findByIdOrNull(orderId)
                ?: throw OrderNotFoundV1Exception(orderId)

        val updatedStampEntity = entity.orderStampEntity?.stampEntity

        return orderMapper.toDTO(entity, updatedStampEntity)
    }

    fun attemptStampCollection(orderId: Long): OrderDTO {
        val order = orderRepository.findByIdOrNull(orderId) ?: throw OrderNotFoundV1Exception(orderId)

        if (order.orderStampEntity?.stampEntity != null) {
            logger.info("Serving stamp from database")
        } else {
            logger.info("Triggering attach stamps for order $orderId...")
            orderStampService.attachStampsToEarliestCreatedAt()
        }

        val updatedOrder = orderRepository.findByIdOrNull(orderId) ?: throw OrderNotFoundV1Exception(orderId)
        val updatedStamp = updatedOrder.orderStampEntity?.stampEntity
        return orderMapper.toDTO(updatedOrder, updatedStamp)
    }
}
