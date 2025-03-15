package com.example.stamp.dtos

import java.time.OffsetDateTime

data class OrderDTO(val id: Long, val createdAt: OffsetDateTime?, val stamp: StampDTO?)
