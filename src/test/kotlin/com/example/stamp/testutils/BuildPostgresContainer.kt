package com.example.stamp.testutils

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

fun buildPostgresContainer(): PostgreSQLContainer<*> {
    return PostgreSQLContainer<Nothing>("postgres:17")
        .apply {
            this.waitingFor(Wait.defaultWaitStrategy())
            this.start()
        }
}
