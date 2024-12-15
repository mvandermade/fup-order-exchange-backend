package com.example.stamp.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "order_stamps")
class OrderStamp(
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,
    // Fetching eagerly because entity graph wasn't working...
    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "stamp_id")
    var stamp: Stamp,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0
}
