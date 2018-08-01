package net.corda.node

import net.corda.commons.events.*
import net.corda.commons.logging.loggerFor
import net.corda.node.api.Node
import net.corda.node.api.events.EventBus
import net.corda.node.api.flows.processing.FlowProcessors
import java.time.Instant
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@Named
internal class RpcServerStub @Inject internal constructor(private val processors: FlowProcessors.Repository, bus: EventBus, override val source: RpcServerStubEventSupport = RpcServerStubEventSupport()) : EventPublisher<RpcServerStub.Event> {

    private companion object {

        private val logger = loggerFor<RpcServerStub>()
        private const val queryTemperatureFlowFQN = "examples.cordapps.one.flows.QueryClusterAverageTemperature"
    }

    init {
        bus.events.filterIsInstance<Node.Event.Initialisation.Completed>().doOnNext { _ -> init() }.subscribe()
    }

    private fun init() {

        logger.info("Initializing RPC server. Flow '${RpcServerStub.queryTemperatureFlowFQN}' is supported by ${processors.forFlow(queryTemperatureFlowFQN).joinToString(", ", "[", "]") { processor -> "'${processor.id}' version '${processor.version}'" }}.")
        source.publish(Event.Invocation("examples.cordapps.one.flows.QueryClusterAverageTemperature", "Bruce Wayne"))
    }

    @Named
    internal class RpcServerStubEventSupport : EventSupport<RpcServerStub.Event>(), EventSource<RpcServerStub.Event>, EventSink<RpcServerStub.Event>

    sealed class Event(id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : net.corda.commons.events.Event(id, createdAt) {

        // User as a String here is grossly simplified, but just to show the idea. Normally, InvocationContext would be part of this.
        class Invocation(val flowName: String, val user: String, id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : Event(id, createdAt)
    }
}