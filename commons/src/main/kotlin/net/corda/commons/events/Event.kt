package net.corda.commons.events

import java.time.Instant

// TODO create a base Event class which fixes equals and hashcode in terms of "id".
interface Event {

    val id: String

    val createdAt: Instant

    // TODO perhaps an InvocationContext here?
}