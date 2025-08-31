package io.github.mvandermade.fup.`order-exchange`.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "order_stamps")
class OrderStampEntity(
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "order_id", nullable = false)
    var orderEntity: OrderEntity,
    // Fetching eagerly because entity graph wasn't working...
    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "stamp_id")
    var stampEntity: StampEntity,
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    var createdAt: OffsetDateTime? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0
}
