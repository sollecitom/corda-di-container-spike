package net.corda.node

import net.corda.commons.events.EventPublisher
import net.corda.commons.events.EventSupport
import net.corda.commons.utils.logging.loggerFor
import net.corda.commons.utils.reactive.only
import net.corda.node.api.Node
import net.corda.node.api.events.EventBus
import net.corda.node.api.flows.processing.FlowProcessors
import java.time.Instant
import java.util.*
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject

@ApplicationScoped
internal class RpcServerStub @Inject internal constructor(private val processors: FlowProcessors.Repository, private val configuration: RpcServerStub.Configuration, private val bus: EventBus, override val source: EventSupport<RpcServerStub.Event>) : EventPublisher<RpcServerStub.Event> {

    private companion object {

        private val logger = loggerFor<RpcServerStub>()
        private const val queryTemperatureFlowFQN = "examples.cordapps.one.flows.QueryClusterAverageTemperature"
    }

    @PostConstruct
    fun setup() {
        bus.events.only<Node.Event.Initialisation.Completed>().doOnNext { _ -> init() }.subscribe()
    }

    private fun init() {
        with(configuration) {
            logger.info("Initializing RPC server on address $networkHost:$networkPort.")
        }

        logger.info("Flow '${RpcServerStub.queryTemperatureFlowFQN}' is supported by ${processors.forFlow(queryTemperatureFlowFQN).joinToString(", ", "[", "]") { processor -> "'${processor.id}' version '${processor.version}'" }}.")
    }

    fun invoke(@Observes evt: Event.Invocation) {
        source.publish(evt)
    }

    @ApplicationScoped
    internal class RpcServerStubEventSupport : EventSupport<RpcServerStub.Event>()

    sealed class Event(id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : net.corda.commons.events.Event(id, createdAt) {

        // User as a String here is grossly simplified, but just to show the idea. Normally, InvocationContext would be part of this.
        class Invocation(val flowName: String, val user: String, id: String = UUID.randomUUID().toString(), createdAt: Instant = Instant.now()) : Event(id, createdAt)
    }

    interface Configuration {

        val networkHost: String
        val networkPort: Int
    }
}