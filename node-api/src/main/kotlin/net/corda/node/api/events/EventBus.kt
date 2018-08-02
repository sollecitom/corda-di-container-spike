package net.corda.node.api.events

import net.corda.commons.events.Event
import reactor.core.publisher.Flux

// TODO try and remove this?
interface EventBus {

    val events: Flux<out Event>
}