package net.corda.commons.events

import reactor.core.publisher.Flux

interface EventSource<out EVENT : Event> {

    val events: Flux<out EVENT>
}

interface EventPublisher<EVENT : Event> {

    val source: EventSource<EVENT>

    val events get() = source.events
}

fun <ORIGINAL, NEW> Flux<ORIGINAL>.filterIsInstance(type: Class<NEW>): Flux<NEW> {

    return filter(type::isInstance).cast(type)
}

inline fun <reified NEW> Flux<*>.filterIsInstance(): Flux<NEW> = filterIsInstance(NEW::class.java)