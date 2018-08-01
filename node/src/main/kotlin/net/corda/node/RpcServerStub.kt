package net.corda.node

import net.corda.commons.events.EventPublisher
import net.corda.commons.events.EventSource
import net.corda.commons.events.filterIsInstance
import net.corda.commons.logging.loggerFor
import net.corda.node.api.Node
import net.corda.node.api.events.EventBus
import net.corda.node.api.flows.processing.FlowProcessors
import reactor.core.publisher.Flux
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Named
internal class RpcServerStub @Inject internal constructor(private val processors: FlowProcessors.Repository, private val bus: EventBus, override val source: RpcServerStubEventSource) : EventPublisher<Node.Event> {

    private companion object {

        private val logger = loggerFor<RpcServerStub>()
        private const val queryTemperatureFlowFQN = "examples.cordapps.one.flows.QueryClusterAverageTemperature"
    }

    // TODO change to use WithLifeCycle
    @PostConstruct
    internal fun initialize() {

        bus.events.filterIsInstance<Node.Event.Initialisation.Completed>().doOnNext { _ -> logProcessorsForQueryTemperatureFlow() }.subscribe()
    }

    private fun logProcessorsForQueryTemperatureFlow() {

        logger.info("Flow '${RpcServerStub.queryTemperatureFlowFQN}' is supported by ${processors.forFlow(queryTemperatureFlowFQN).joinToString(", ", "[", "]") { processor -> "'${processor.id}' version '${processor.version}'" }}.")
    }

    @Named
    internal class RpcServerStubEventSource : EventSource<Node.Event> {

        override val events: Flux<out Node.Event> = Flux.empty()
    }
}