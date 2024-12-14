package com.example.stamp.daemons

import com.example.stamp.providers.RandomProvider
import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException

@SpringBootTest
class StampGeneratorDaemonTest(
    @Autowired private val stampRepository: StampRepository,
    @Autowired private val stampGeneratorDaemon: StampGeneratorDaemon,
) {
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

    @MockkBean
    private lateinit var randomProvider: RandomProvider

    @Test
    fun `Should persist code`() {
        every { randomProvider.randomString(any()) } returns "test"
        stampGeneratorDaemon.persistRandomStamp()
    }

    @Test
    fun `Expect error code is not unique`() {
        every { randomProvider.randomString(any()) } returns "test"

        stampGeneratorDaemon.persistRandomStamp()
        assertThrows<DataIntegrityViolationException> {
            stampGeneratorDaemon.persistRandomStamp()
        }
    }
}
