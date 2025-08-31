package io.github.mvandermade.fup.order.exchange.services

import com.ninjasquad.springmockk.MockkBean
import io.github.mvandermade.fup.order.exchange.providers.EmojiProvider
import io.github.mvandermade.fup.order.exchange.repositories.StampRepository
import io.github.mvandermade.fup.order.exchange.testutils.buildPostgresContainer
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class StampServiceTest(
    @param:Autowired private val stampService: StampService,
) {
    @MockkBean
    private lateinit var emojiProviderMock: EmojiProvider

    @MockitoSpyBean
    private lateinit var stampRepositorySpyk: StampRepository

    @Test
    fun `Should persist code`() {
        every { emojiProviderMock.randomEmoji() } returns "test"
        stampService.persistRandomStamp()
    }

    @Test
    fun `Expect no error code is not unique because of null check`() {
        every { emojiProviderMock.randomEmoji() } returns "test"

        stampService.persistRandomStamp()
        stampService.persistRandomStamp()
    }

    @Test
    fun `Expect error code is not unique`() {
        every { emojiProviderMock.randomEmoji() } returns "test"

        stampService.persistRandomStamp()

        doReturn(null).`when`(stampRepositorySpyk).findByCode("test")
        assertThrows<DataIntegrityViolationException> {
            stampService.persistRandomStamp()
        }
    }

    companion object {
        @Container
        @ServiceConnection
        val postgresContainer = buildPostgresContainer()
    }
}
