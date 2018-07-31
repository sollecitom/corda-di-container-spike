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

        events.stream.filterIsInstance<NodeEvents.Initialisation.Completed>().doOnNext { _ -> logProcessorsForQueryTemperatureFlow() }.subscribe()
    }

    private fun logProcessorsForQueryTemperatureFlow() {

        logger.info("Flow '${RpcServerStub.queryTemperatureFlowFQN}' is supported by ${flowsProcessorRegistry.processorsForFlow(queryTemperatureFlowFQN).joinToString(", ", "[", "]") { processor -> "'${processor.id}' version '${processor.version}'" }}.")
    }
}