package net.corda.commons.events

import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.io.Closeable

abstract class EventSupport<EVENT : Event> : EventSource<EVENT>, EventSink<EVENT>, Closeable {

    private val processor = EmitterProcessor.create<EVENT>()

    override val events: Flux<EVENT> = processor
    // This could force subscribers to run on a separate thread, to avoid deadlocks.
//    override val events: Flux<EVENT> = processor.publishOn(Schedulers.parallel())

    override fun publish(event: EVENT) = processor.onNext(event)

    override fun close() {

        processor.onComplete()
    }
}