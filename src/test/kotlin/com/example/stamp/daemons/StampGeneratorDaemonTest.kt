package com.example.stamp.daemons

import com.example.stamp.providers.RandomProvider
import com.example.stamp.repositories.StampRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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
