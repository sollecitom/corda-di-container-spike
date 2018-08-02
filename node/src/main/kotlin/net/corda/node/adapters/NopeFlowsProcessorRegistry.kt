package net.corda.node.adapters

import net.corda.commons.di.DefaultBehaviour
import net.corda.commons.utils.logging.loggerFor
import net.corda.node.api.flows.processing.FlowProcessor
import net.corda.node.api.flows.processing.FlowProcessors
import javax.annotation.PostConstruct
import javax.inject.Named

@DefaultBehaviour
@Named
internal class NopeFlowsProcessorRegistry : FlowProcessors.Registry, FlowProcessors.Repository {

    private companion object {

        private val logger = loggerFor<NopeFlowsProcessorRegistry>()
    }

    @PostConstruct
    private fun logInit() {

        // This still happens, regardless of the priority.
        logger.info("Initializing NopeFlowsProcessorRegistry")
    }

    override fun register(processor: FlowProcessor) {

        logger.info("Nope, no registration!")
    }

    override fun forFlow(initiatingFlowName: String): Set<FlowProcessor> {

        logger.info("Nope, no looking up processors for flows!")
        return emptySet()
    }
}