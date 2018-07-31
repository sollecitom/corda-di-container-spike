package net.corda.node.api.events

import net.corda.commons.events.Event
import java.time.Instant
import java.time.Instant.now
import java.util.*

object NodeEvents {

    object Initialisation {

        // TODO maybe use time-based UUID
        class Completed(override val id: String = UUID.randomUUID().toString(), override val createdAt: Instant = now()) : Event {


            override fun equals(other: Any?): Boolean {

                if (this === other) {
                    return true
                }
                if (javaClass != other?.javaClass) {
                    return false
                }

                other as NodeEvents.Initialisation.Completed

                if (id != other.id) {
                    return false
                }

                return true
            }

            override fun hashCode(): Int {

                return id.hashCode()
            }
        }
    }
}