package com.example.stamp.services

import com.example.stamp.entities.Order
import com.example.stamp.entities.OrderStamp
import com.example.stamp.entities.Stamp
import com.example.stamp.exceptions.OrderNotConfirmedException
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampRepository
import io.mockk.mockk
import nl.wykorijnsburger.kminrandom.minRandom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StampServiceTest(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val stampRepository: StampRepository,
    @Autowired private val orderStampRepository: OrderStampRepository,
) {
    private val orderStampService = mockk<OrderStampService>()

    private val stampService =
        StampService(
            orderRepository = orderRepository,
            orderStampService = orderStampService,
        )

    @BeforeEach
    fun setUp(
        @Autowired orderRepository: OrderRepository,
        @Autowired orderStampRepository: OrderStampRepository,
        @Autowired stampRepository: StampRepository,
    ) {
        orderStampRepository.deleteAllInBatch()
        stampRepository.deleteAllInBatch()
        orderRepository.deleteAllInBatch()
    }

    @Test
    fun `Cannot collect because order is not confirmed`() {
        val order =
            orderRepository.save(
                minRandom<Order>().apply {
                    orderConfirmed = false
                },
            )

        assertThrows<OrderNotConfirmedException> { stampService.attemptStampCollection(order.id) }
    }

    @Test
    fun `Can collect stamp code from database`() {
        val order =
            orderRepository.save(
                minRandom<Order>().apply {
                    orderConfirmed = true
                },
            )
        val stamp =
            stampRepository.save(
                minRandom<Stamp>().apply {
                    this.code = "ABCD"
                },
            )
        orderStampRepository.save(
            OrderStamp(order, stamp),
        )

        val collectedStamp = stampService.attemptStampCollection(order.id)

        assertThat(collectedStamp.code).isEqualTo("ABCD")
    }
}
