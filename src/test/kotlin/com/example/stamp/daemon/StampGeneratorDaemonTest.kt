package com.example.stamp.daemon

import com.example.stamp.repository.StampRepository
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

    @Test
    fun `Should persist code`() {
        stampGeneratorDaemon.persistPostzegel("A")
    }

    @Test
    fun `Expect error code is not unique`() {
        stampGeneratorDaemon.persistPostzegel("A")
        assertThrows<DataIntegrityViolationException> {
            stampGeneratorDaemon.persistPostzegel("A")
        }
    }
}
