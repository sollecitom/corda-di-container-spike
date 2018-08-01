package net.corda.node.impl

import net.corda.commons.events.Event
import net.corda.commons.events.EventSource
import net.corda.node.api.events.EventBus
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.io.Closeable
import java.time.Duration
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Named

// This would normally be in a separate module depending on commons and available to adapters as well, not in node-api. The package "impl" should not exist as well.
@Named
class MultiplexingEventBus @Inject constructor(sources: List<EventSource<Event>>) : EventBus, Closeable {

    private companion object {
        private val EVENTS_LOG_TTL = Duration.ofSeconds(5)
    }

    private val processor = EmitterProcessor.create<Event>()

    // This is to ensure a subscriber receives events that were published up to 5 seconds before. It helps during initialisation.
    override val events: Flux<Event> = processor.cache(EVENTS_LOG_TTL)
    // This could force subscribers to run on a separate thread, to avoid deadlocks.
//    override val events: Flux<Event> = processor.publishOn(Schedulers.parallel())

    init {
        val stream = sources.map(EventSource<Event>::events).foldRight<Flux<out Event>, Flux<Event>>(Flux.empty()) { current, accumulator -> accumulator.mergeWith(current) }
        stream.subscribe { event -> processor.onNext(event) }
    }

    @PreDestroy
    override fun close() = processor.onComplete()
}