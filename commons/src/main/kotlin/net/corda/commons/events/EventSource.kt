package net.corda.commons.events

import reactor.core.publisher.Flux

// TODO get rid of usages of this type, expose events from each type with an interface, create an event bus that finds all beans implementing the interface and multiplexes the streams.
interface EventSource<EVENT : Event> {

    val events: Flux<out EVENT>
}

@Suppress("UNCHECKED_CAST")
fun <ORIGINAL, NEW> Flux<ORIGINAL>.filterIsInstance(type: Class<*>): Flux<NEW> {

    return filter(type::isInstance).map { element -> element as NEW }
}

inline fun <reified NEW> Flux<*>.filterIsInstance(): Flux<NEW> = filterIsInstance(NEW::class.java)