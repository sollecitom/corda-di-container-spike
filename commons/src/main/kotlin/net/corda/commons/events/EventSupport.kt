package net.corda.commons.events

import reactor.core.publisher.EmitterProcessor
import java.io.Closeable

abstract class EventSupport<EVENT : Event> : EventSource<EVENT>, EventSink<EVENT>, Closeable {

    override val events: EmitterProcessor<EVENT> = EmitterProcessor.create()

    override fun publish(event: EVENT) = events.onNext(event)

    override fun close() {

        events.onComplete()
    }
}