package net.corda.commons.utils.reactive

import reactor.core.publisher.Flux

fun <ORIGINAL, NEW> Flux<ORIGINAL>.only(type: Class<NEW>): Flux<NEW> {

    return filter(type::isInstance).cast(type)
}

inline fun <reified NEW> Flux<*>.only(): Flux<NEW> = only(NEW::class.java)