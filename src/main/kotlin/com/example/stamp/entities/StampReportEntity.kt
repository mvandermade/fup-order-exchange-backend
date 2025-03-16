package com.example.stamp.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "stamp_reports")
class StampReportEntity(
    var code: String,
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    var createdAt: OffsetDateTime? = null,
    var createdAtObserver: OffsetDateTime? = null,
    var reachedDestination: Boolean? = null,
    var comment: String? = null,
    var deletionIsDone: Boolean = false,
    var comparisonIsError: Boolean = false,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0
}
