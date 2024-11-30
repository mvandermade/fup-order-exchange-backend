package com.example.stamp.daemon

import com.example.stamp.provider.RandomProvider
import com.example.stamp.repository.StampRepository
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
    fun before() {
        stampRepository.deleteAll()
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
