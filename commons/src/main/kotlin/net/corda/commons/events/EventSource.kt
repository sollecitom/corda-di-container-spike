package net.corda.commons.events

import reactor.core.publisher.Flux

interface EventSource<out EVENT : Event> {

    val events: Flux<out EVENT>
}