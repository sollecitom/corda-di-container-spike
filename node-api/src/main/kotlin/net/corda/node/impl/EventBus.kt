package net.corda.node.impl

import net.corda.commons.events.Event
import net.corda.commons.events.PublishEvent
import net.corda.commons.events.EventSource
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.io.Closeable
import javax.annotation.PreDestroy
import javax.inject.Named

// TODO this should be in a separate module depending on commons and available to adapters as well, not in node-api. The package "impl" should be deleted too.
@Named
class EventBus : EventSource, PublishEvent, Closeable {

    private val events = EmitterProcessor.create<Event>()

    // TODO evaluate whether this should use Schedulers.Io() to make processing non-blocking.
    override val stream: Flux<out Event> = events

    override fun invoke(event: Event) = events.onNext(event)

    @PreDestroy
    override fun close() = events.onComplete()
}