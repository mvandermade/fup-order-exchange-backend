package io.github.mvandermade.fup.`order-exchange`.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@Profile("!test")
@EnableScheduling
class SchedulingConfiguration
