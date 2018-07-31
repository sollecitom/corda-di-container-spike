package net.corda.node

import net.corda.commons.events.EventSource
import net.corda.commons.events.filterIsInstance
import net.corda.commons.logging.loggerFor
import net.corda.node.api.events.NodeEvents
import net.corda.node.api.flows.processing.registry.FlowsProcessorRegistry
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Named
internal class RpcServerStub @Inject internal constructor(private val flowsProcessorRegistry: FlowsProcessorRegistry, private val events: EventSource) {

    private companion object {

        private val logger = loggerFor<RpcServerStub>()
        private const val queryTemperatureFlowFQN = "examples.cordapps.one.flows.QueryClusterAverageTemperature"
    }

    @PostConstruct
    internal fun initialize() {

        events.stream
                .filterIsInstance<NodeEvents.Initialisation.Completed>()
                .doOnNext { _ -> flowsProcessorRegistry.processorsForFlow(queryTemperatureFlowFQN).forEach { processor -> logger.info("Flow '$queryTemperatureFlowFQN' is supported by '${processor.id}' version '${processor.version}'.") } }
                .subscribe()
    }
}