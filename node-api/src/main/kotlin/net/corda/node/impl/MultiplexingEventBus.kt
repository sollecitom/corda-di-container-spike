package net.corda.node.impl

import net.corda.commons.events.Event
import net.corda.commons.events.EventSource
import net.corda.commons.utils.logging.loggerFor
import net.corda.node.api.events.EventBus
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance
import javax.inject.Inject

// This would normally be in a separate module depending on commons and available to adapters as well, not in node-api. The package "impl" should not exist as well.
// It is not in module "node" to stay available for adapters modules.
@ApplicationScoped
class MultiplexingEventBus @Inject constructor(private val sources: Instance<EventSource<*>>) : EventBus, AutoCloseable {

    private companion object {
        private val EVENTS_LOG_TTL = Duration.ofSeconds(5)
        private val log = loggerFor<MultiplexingEventBus>()
    }

    private val processor = EmitterProcessor.create<Event>()

    // This is to ensure a subscriber receives events that were published up to 5 seconds before. It helps during initialisation.
    // The trailing `publishOn(Schedulers.parallel())` is to force subscribers to run on a thread pool, to avoid deadlocks.
    override val events: Flux<Event> = processor.cache(EVENTS_LOG_TTL).publishOn(Schedulers.parallel())

    @PostConstruct
    fun setup() {
        val stream = sources.map(EventSource<Event>::events).foldRight<Flux<out Event>, Flux<Event>>(Flux.empty()) { current, accumulator -> accumulator.mergeWith(current) }
        stream.subscribe { event -> processor.onNext(event) }
    }

    @PreDestroy
    override fun close() = processor.onComplete()
}