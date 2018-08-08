package net.corda.commons.events

import java.time.Instant
import java.util.*

// Time-based UUIDs are better than random-based, but for the sake of the exercise it is fine as it is.
abstract class Event(val id: String = UUID.randomUUID().toString(), val createdAt: Instant = Instant.now()) {

    // InvocationContext would be here, perhaps as an optional field, to allow correlation.

    final override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        other as Event
        return (id == other.id)
    }

    final override fun hashCode(): Int {
        return id.hashCode()
    }
}