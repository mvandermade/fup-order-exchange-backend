package com.example.stamp.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.proxy.HibernateProxy

@Entity
@Table(name = "stamp_report_idempotency_keys")
class StampReportIdempotencyKeyEntity(
    @Column(name = "user_key", unique = true)
    var userKey: String,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "stamp_report_id", nullable = true)
    var stampReport: StampReportEntity,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stamp_report_idempotency_keys_gen")
    @SequenceGenerator(name = "stamp_report_idempotency_keys_gen", sequenceName = "stamp_report_idempotency_keys_seq")
    @Column(nullable = false)
    var id: Long = 0

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as StampReportIdempotencyKeyEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()
}
