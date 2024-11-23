package com.example.stamp.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version

@Entity
@Table(name = "stamps")
class StampEntity(
    @Version
    var version: Long? = null,
    @Column(unique = true)
    var code: String = "",
    @Column
    var timeMillis: Long = 0L,
    @Column(length = 36)
    var orderId: String? = null,
) {
    @Id
    @GeneratedValue
    var id: Long = 0
}
