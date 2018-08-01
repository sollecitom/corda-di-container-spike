package net.corda.node

import net.corda.commons.events.filterIsInstance
import net.corda.commons.logging.loggerFor
import net.corda.node.api.events.EventBus
import net.corda.node.api.NodeEvents
import net.corda.node.api.flows.processing.FlowProcessors
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Named
internal class RpcServerStub @Inject internal constructor(private val processors: FlowProcessors.Repository, private val bus: EventBus) {

    private companion object {

        private val logger = loggerFor<RpcServerStub>()
        private const val queryTemperatureFlowFQN = "examples.cordapps.one.flows.QueryClusterAverageTemperature"
    }

    @PostConstruct
    internal fun initialize() {

        bus.events.filterIsInstance<NodeEvents.Initialisation.Completed>().doOnNext { _ -> logProcessorsForQueryTemperatureFlow() }.subscribe()
    }

    private fun logProcessorsForQueryTemperatureFlow() {

        logger.info("Flow '${RpcServerStub.queryTemperatureFlowFQN}' is supported by ${processors.forFlow(queryTemperatureFlowFQN).joinToString(", ", "[", "]") { processor -> "'${processor.id}' version '${processor.version}'" }}.")
    }
}