package com.example.stamp.models

import java.time.OffsetDateTime

data class Order(val id: Long, val createdAt: OffsetDateTime?, val orderConfirmed: Boolean)
