package net.corda.node

import net.corda.commons.events.EventPublisher
import net.corda.commons.events.PublishingEventSource
import net.corda.commons.utils.logging.loggerFor
import net.corda.commons.utils.reactive.only
import net.corda.node.api.Node
import net.corda.node.api.events.EventBus
import net.corda.node.api.flows.processing.FlowProcessors
import java.time.Instant
import java.util.*
import javax.inject.Inject
import javax.inject.Named

private const val eventSourceQualifier = "RpcServerStubPublishingEventSource"

@Named
internal class RpcServerStub @Inject internal constructor(private val processors: FlowProcessors.Repository, private val configuration: RpcServerStub.Configuration, bus: EventBus, @Named(eventSourceQualifier) override val source: PublishingEventSource<RpcServerStub.Event> = RpcServerStubPublishingEventSource()) : EventPublisher<RpcServerStub.Event> {

    private companion object {

        private val logger = loggerFor<RpcServerStub>()
        private const val queryTemperatureFlowFQN = "examples.cordapps.one.flows.QueryClusterAverageTemperature"
    }

    init {
        bus.events.only<Node.Event.Initialisation.Completed>().doOnNext { _ -> init() }.subscribe()
    }

    private fun init() {

        with(configuration) {
            logger.info("Initializing RPC server on address $networkHost:$networkPort.")
        }

        logger.info("Flow '${RpcServerStub.queryTemperatureFlowFQN}' is supported by ${processors.forFlow(queryTemperatureFlowFQN).joinToString(", ", "[", "]") { processor -> "'${processor.id}' version '${processor.version}'" }}.")

        source.publish(Event.Invocation("examples.cordapps.one.flows.QueryClusterAverageTemperature", "Bruce Wayne"))
    }

    sealed class Event(id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : net.corda.commons.events.Event(id, createdAt) {

        // User as a String here is grossly simplified, but just to show the idea. Normally, InvocationContext would be part of this.
        class Invocation(val flowName: String, val user: String, id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : Event(id, createdAt)
    }

    interface Configuration {

        val networkHost: String
        val networkPort: Int
    }

    @Named
    private class RpcServerStubPublishingEventSource : PublishingEventSource<RpcServerStub.Event>()
}