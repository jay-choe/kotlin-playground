package com.jay.playground.repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.util.UUID

@Entity
@Table(uniqueConstraints = [UniqueConstraint(name = "uk_event_id", columnNames = ["eventId"])])
class EventId(
    val eventId: UUID
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (other !is EventId) {
            return false
        }

        return eventId == other
    }

    override fun hashCode(): Int {
        return this.eventId.hashCode()
    }
}
