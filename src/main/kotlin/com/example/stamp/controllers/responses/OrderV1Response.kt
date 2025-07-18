package com.example.stamp.controllers.responses

import com.example.stamp.dtos.StampDTO

data class OrderV1Response(
    val id: Long,
    val stamp: StampDTO? = null,
)
