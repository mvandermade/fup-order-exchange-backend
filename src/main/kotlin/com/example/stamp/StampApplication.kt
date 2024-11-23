package com.example.stamp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class StampApplication

fun main(args: Array<String>) {
    runApplication<StampApplication>(*args)
}
