package net.corda.node.api.events

import net.corda.commons.events.Event
import reactor.core.publisher.Flux

interface EventBus {

    val events: Flux<out Event>
}