package com.example.stamp.daemons

import com.example.stamp.providers.RandomProvider
import com.example.stamp.repositories.StampRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class StampGeneratorDaemonTest(
    @Autowired private val stampGeneratorDaemon: StampGeneratorDaemon,
) {
    @MockkBean
    private lateinit var randomProviderMock: RandomProvider

    @MockitoSpyBean
    private lateinit var stampRepositorySpyk: StampRepository

    @Test
    fun `Should persist code`() {
        every { randomProviderMock.randomString(any()) } returns "test"
        stampGeneratorDaemon.persistRandomStamp()
    }

    @Test
    fun `Expect no error code is not unique because of null check`() {
        every { randomProviderMock.randomString(any()) } returns "test"

        stampGeneratorDaemon.persistRandomStamp()
        stampGeneratorDaemon.persistRandomStamp()
    }

    @Test
    fun `Expect error code is not unique`() {
        every { randomProviderMock.randomString(any()) } returns "test"

        stampGeneratorDaemon.persistRandomStamp()

        doReturn(null).`when`(stampRepositorySpyk).findByCode("test")
        assertThrows<DataIntegrityViolationException> {
            stampGeneratorDaemon.persistRandomStamp()
        }
    }

    companion object {
        @Container
        @ServiceConnection
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:17")
    }
}
