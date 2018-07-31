package net.corda.node.adapters

import net.corda.commons.di.DefaultBehaviour
import net.corda.commons.logging.loggerFor
import net.corda.node.api.flows.processing.FlowProcessor
import net.corda.node.api.flows.processing.registry.FlowsProcessorRegistry
import javax.annotation.PostConstruct
import javax.inject.Named

@DefaultBehaviour
@Named
class NopeFlowsProcessorRegistry : FlowsProcessorRegistry {

    private companion object {

        private val logger = loggerFor<NopeFlowsProcessorRegistry>()
    }

    @PostConstruct
    private fun start() {

        // This still happens, regardless of the priority.
        logger.info("Initializing NopeFlowsProcessorRegistry")
    }

    override fun register(processor: FlowProcessor) {

        logger.info("Nope, no registration!")
    }

    override fun processorsForFlow(initiatingFlowName: String): Set<FlowProcessor> {

        logger.info("Nope, no looking up processors for flows!")
        return emptySet()
    }
}