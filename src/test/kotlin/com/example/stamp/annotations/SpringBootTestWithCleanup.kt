package com.example.stamp.annotations

import com.example.stamp.extensions.CleanUpBeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ExtendWith(CleanUpBeforeEach::class)
annotation class SpringBootTestWithCleanup
