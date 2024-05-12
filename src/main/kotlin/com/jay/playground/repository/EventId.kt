package com.jay.playground.repository

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
class EventId(
    @Id val eventId: UUID
) {

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
