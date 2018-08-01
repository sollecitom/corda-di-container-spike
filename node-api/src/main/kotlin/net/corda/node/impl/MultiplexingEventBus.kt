package net.corda.node.impl

import net.corda.commons.events.Event
import net.corda.commons.events.PublishEvent
import net.corda.node.api.events.EventBus
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.io.Closeable
import javax.annotation.PreDestroy
import javax.inject.Named

// TODO this should be in a separate module depending on commons and available to adapters as well, not in node-api. The package "impl" should be deleted too.
@Named
class MultiplexingEventBus : EventBus, PublishEvent, Closeable {

    private val stream = EmitterProcessor.create<Event>()

    // TODO evaluate whether this should use Schedulers.Io() to make processing non-blocking.
    override val events: Flux<out Event> = stream

    override fun invoke(event: Event) = stream.onNext(event)

    @PreDestroy
    override fun close() = stream.onComplete()
}