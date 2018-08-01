package net.corda.node.api

import net.corda.commons.lifecycle.WithLifeCycle
import java.time.Instant
import java.util.*

interface Node : WithLifeCycle {

    sealed class Event(id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : net.corda.commons.events.Event(id, createdAt) {

        sealed class Initialisation(id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : Event(id, createdAt) {

            class Completed(id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : Initialisation(id, createdAt)
        }
    }
}