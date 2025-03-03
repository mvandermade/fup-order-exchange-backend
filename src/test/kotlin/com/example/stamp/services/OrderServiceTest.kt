package com.example.stamp.services

import com.example.stamp.entities.OrderEntity
import com.example.stamp.entities.OrderStampEntity
import com.example.stamp.entities.StampEntity
import com.example.stamp.exceptions.OrderNotConfirmedV1Exception
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampRepository
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class OrderServiceTest(
    @Autowired val orderService: OrderService,
    @Autowired val orderRepository: OrderRepository,
    @Autowired val stampRepository: StampRepository,
    @Autowired val orderStampRepository: OrderStampRepository,
) {
    @Test
    fun `Cannot collect because order is not confirmed`() {
        val orderEntity =
            orderRepository.save(
                minRandom<OrderEntity>().apply {
                    orderIsConfirmed = false
                },
            )

        assertThrows<OrderNotConfirmedV1Exception> { orderService.attemptStampCollection(orderEntity.id) }
    }

    @Test
    fun `Can collect stamp code from database`() {
        val orderEntity =
            orderRepository.save(
                minRandom<OrderEntity>().apply {
                    orderIsConfirmed = true
                },
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
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:17")
    }
}
