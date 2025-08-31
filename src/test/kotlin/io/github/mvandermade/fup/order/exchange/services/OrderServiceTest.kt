package io.github.mvandermade.fup.order.exchange.services

import io.github.mvandermade.fup.order.exchange.entities.OrderEntity
import io.github.mvandermade.fup.order.exchange.entities.OrderIdempotencyKeyEntity
import io.github.mvandermade.fup.order.exchange.entities.OrderStampEntity
import io.github.mvandermade.fup.order.exchange.entities.StampEntity
import io.github.mvandermade.fup.order.exchange.repositories.OrderIdempotencyKeyRepository
import io.github.mvandermade.fup.order.exchange.repositories.OrderRepository
import io.github.mvandermade.fup.order.exchange.repositories.OrderStampRepository
import io.github.mvandermade.fup.order.exchange.repositories.StampRepository
import io.github.mvandermade.fup.order.exchange.testutils.buildPostgresContainer
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.dao.DataIntegrityViolationException
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class OrderServiceTest(
    @param:Autowired val orderService: OrderService,
    @param:Autowired val orderRepository: OrderRepository,
    @param:Autowired val stampRepository: StampRepository,
    @param:Autowired val orderStampRepository: OrderStampRepository,
    @param:Autowired val orderIdempotencyKeyRepository: OrderIdempotencyKeyRepository,
) {
    @BeforeEach
    fun setUp() {
        orderIdempotencyKeyRepository.deleteAll()
        orderRepository.deleteAll()
    }

    @Test
    fun `Idempotency key save failure is transactional test`() {
        val order = orderRepository.save(minRandom())

        orderIdempotencyKeyRepository.save(
            OrderIdempotencyKeyEntity(
                userKey = "123",
                order = order,
            ),
        )
        assertThrows<DataIntegrityViolationException> {
            orderService.postOrder(idempotentUserKey = "123")
        }

        assertThat(orderRepository.count()).isEqualTo(1)
    }

    @Test
    fun `Idempotency key is saved ok`() {
        val dto = orderService.postOrder(idempotentUserKey = "123")

        val idpEntity = orderIdempotencyKeyRepository.findByUserKey("123")

        assertThat(idpEntity?.order?.id).isEqualTo(dto.id)
    }

    @Test
    fun `Can collect stamp code from database`() {
        val orderEntity =
            orderRepository.save(
                minRandom<OrderEntity>(),
            )
        val stampEntity =
            stampRepository.save(
                minRandom<StampEntity>().apply {
                    this.code = "ABCD"
                },
            )
        orderStampRepository.save(
            OrderStampEntity(orderEntity, stampEntity),
        )

        val collectedOrder = orderService.attemptStampCollection(orderEntity.id)

        assertThat(collectedOrder.stamp?.code).isEqualTo("ABCD")
    }

    companion object {
        @Container
        @ServiceConnection
        @Suppress("unused")
        val postgresContainer = buildPostgresContainer()
    }
}
