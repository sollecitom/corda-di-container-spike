package net.corda.node.impl

import net.corda.commons.events.Event
import net.corda.commons.events.EventSource
import net.corda.commons.events.PublishEvent
import net.corda.node.api.events.EventBus
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.io.Closeable
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Named

// This would normally be in a separate module depending on commons and available to adapters as well, not in node-api. The package "impl" should not exist as well.
@Named
class MultiplexingEventBus @Inject constructor(sources: List<EventSource<out Event>>) : EventBus, PublishEvent, Closeable {

    override val events: EmitterProcessor<Event> = EmitterProcessor.create<Event>()

    init {
        val stream = sources.map(EventSource<out Event>::events).foldRight<Flux<out Event>, Flux<Event>>(Flux.empty()) { current, accumulator -> accumulator.mergeWith(current) }
        stream.subscribe(events::onNext)
    }

    // TODO check whether this is needed (think about Cordapps and plugins)
    override fun invoke(event: Event) = events.onNext(event)

    @PreDestroy
    override fun close() = events.onComplete()
}