package net.corda.node.api

import net.corda.commons.events.Event
import java.time.Instant
import java.time.Instant.now
import java.util.*

object NodeEvents {

    object Initialisation {

        class Completed constructor(id: String = UUID.randomUUID().toString(), createdAt: Instant = now()) : Event(id, createdAt)
    }
}