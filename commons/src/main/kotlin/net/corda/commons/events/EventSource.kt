package net.corda.commons.events

import reactor.core.publisher.Flux

interface EventSource {

    val stream: Flux<out Event>
}

@Suppress("UNCHECKED_CAST")
fun <ORIGINAL, NEW> Flux<ORIGINAL>.filterIsInstance(type: Class<*>): Flux<NEW> {

    return filter(type::isInstance).map { element -> element as NEW }
}

inline fun <reified NEW> Flux<*>.filterIsInstance(): Flux<NEW> = filterIsInstance(NEW::class.java)