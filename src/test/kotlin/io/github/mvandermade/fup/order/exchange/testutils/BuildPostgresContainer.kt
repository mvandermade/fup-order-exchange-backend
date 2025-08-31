package io.github.mvandermade.fup.order.exchange.testutils

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

fun buildPostgresContainer(): PostgreSQLContainer<*> =
    PostgreSQLContainer<Nothing>("postgres:17")
        .apply {
            this.waitingFor(Wait.defaultWaitStrategy())
            this.start()
        }
