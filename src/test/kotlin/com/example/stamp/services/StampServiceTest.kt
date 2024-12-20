package com.example.stamp.services

import com.example.stamp.annotations.SpringBootTestWithCleanup
import com.example.stamp.entities.OrderEntity
import com.example.stamp.entities.OrderStampEntity
import com.example.stamp.entities.StampEntity
import com.example.stamp.exceptions.OrderNotAcknowledgedV1Exception
import com.example.stamp.mappers.StampMapper
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampRepository
import io.mockk.mockk
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTestWithCleanup
class StampServiceTest(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val stampRepository: StampRepository,
    @Autowired private val orderStampRepository: OrderStampRepository,
    @Autowired private val stampMapper: StampMapper,
) {
    private val orderStampService = mockk<OrderStampService>()

    private val stampService =
        StampService(
            orderRepository = orderRepository,
            orderStampService = orderStampService,
            stampMapper = stampMapper,
        )

    @Test
    fun `Cannot collect because order is not acknowledged`() {
        val orderEntity =
            orderRepository.save(
                minRandom<OrderEntity>().apply {
                    orderIsAcknowledged = false
                },
            )

        assertThrows<OrderNotAcknowledgedV1Exception> { stampService.attemptStampCollection(orderEntity.id) }
    }

    @Test
    fun `Can collect stamp code from database`() {
        val orderEntity =
            orderRepository.save(
                minRandom<OrderEntity>().apply {
                    orderIsAcknowledged = true
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

        val collectedStamp = stampService.attemptStampCollection(orderEntity.id)

        assertThat(collectedStamp.code).isEqualTo("ABCD")
    }
}
