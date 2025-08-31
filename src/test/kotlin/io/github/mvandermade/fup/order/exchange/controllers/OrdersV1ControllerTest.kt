package io.github.mvandermade.fup.order.exchange.controllers

import io.github.mvandermade.fup.order.exchange.dtos.OrderIdempotencyKeyDTO
import io.github.mvandermade.fup.order.exchange.mappers.OrderMapper
import io.github.mvandermade.fup.order.exchange.services.OrderIdempotencyKeyService
import io.github.mvandermade.fup.order.exchange.services.OrderService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.wykorijnsburger.kminrandom.minRandom
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class OrdersV1ControllerTest {
    private val orderService = mockk<OrderService>()
    private val orderMapper = mockk<OrderMapper>()
    private val orderIdempotencyKeyService = mockk<OrderIdempotencyKeyService>()

    private val controller =
        OrdersV1Controller(
            orderService,
            orderMapper,
            orderIdempotencyKeyService,
        )

    @Nested
    inner class PostOrder {
        @Test
        fun `No idempotency key in dbpost the order`() {
            every { orderIdempotencyKeyService.getIdempotencyKeyDTO("abc") } returns null
            every { orderMapper.toResponse(any()) } returns minRandom()
            every { orderService.postOrder("abc") } returns minRandom()
            controller.postOrder("abc")
            verify(exactly = 1) { orderService.postOrder("abc") }
        }

        @Test
        fun `Idempotency key in db return the order`() {
            every { orderIdempotencyKeyService.getIdempotencyKeyDTO("abc") } returns minRandom<OrderIdempotencyKeyDTO>().copy(orderId = 123)
            every { orderMapper.toResponse(any()) } returns minRandom()
            every { orderService.getOrder(123) } returns minRandom()
            controller.postOrder("abc")
            verify(exactly = 1) { orderService.getOrder(123) }
        }
    }
}
