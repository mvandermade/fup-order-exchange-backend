package com.example.stamp.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.hibernate.proxy.HibernateProxy

@Entity
@Table(name = "stamps")
class Stamp(
    @Version
    var version: Long? = null,
    @Column(unique = true)
    var code: String = "",
    @Column
    var timeMillis: Long = 0L,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.DETACH])
    var order: Order? = null,
) {
    @Id
    @GeneratedValue
    var id: Long = 0

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as Stamp

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()
}
