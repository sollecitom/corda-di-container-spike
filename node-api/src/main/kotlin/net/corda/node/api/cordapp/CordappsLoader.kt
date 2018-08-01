package net.corda.node.api.cordapp

import net.corda.commons.events.EventPublisher
import java.time.Instant
import java.util.*

interface CordappsLoader : EventPublisher<CordappsLoader.Event> {

    val cordapps: Set<Cordapp>

    sealed class Event(id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : net.corda.commons.events.Event(id, createdAt) {

        class CordappsWereLoaded(val loaded: Set<Cordapp>, id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : Event(id, createdAt) {

            constructor(first: Cordapp, vararg others: Cordapp, id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : this(setOf(first, *others), id, createdAt)
        }

        class CordappsWereUnloaded(val unloaded: Set<CordappInfo>, id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : Event(id, createdAt) {

            constructor(first: CordappInfo, vararg others: CordappInfo, id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : this(setOf(first, *others), id, createdAt)

            data class CordappInfo(val name: String, val version: Int)
        }
    }
}