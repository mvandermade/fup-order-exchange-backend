package com.example.stamp.services

import com.example.stamp.entities.OrderEntity
import com.example.stamp.entities.OrderIdempotencyKeyEntity
import com.example.stamp.entities.OrderStampEntity
import com.example.stamp.entities.StampEntity
import com.example.stamp.repositories.OrderIdempotencyKeyRepository
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampRepository
import com.example.stamp.testutils.buildPostgresContainer
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
    @Autowired val orderService: OrderService,
    @Autowired val orderRepository: OrderRepository,
    @Autowired val stampRepository: StampRepository,
    @Autowired val orderStampRepository: OrderStampRepository,
    @Autowired val orderIdempotencyKeyRepository: OrderIdempotencyKeyRepository,
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
