package net.corda.cordapp.api.flows.registry

import net.corda.commons.logging.loggerFor
import net.corda.node.api.flows.processing.FlowProcessor
import javax.inject.Named

@Named
internal class ConsoleNewBindingLogger : InMemoryFlowsProcessorRegistry.LogNewBinding {

    private companion object {

        private val logger = loggerFor<ConsoleNewBindingLogger>()
    }

    override fun apply(initiating: String, processor: FlowProcessor, allInitiatedFlows: Set<FlowProcessor>) {

        logger.info("New processor ${processor.description()} registered for initiating flow '$initiating'. Bindings are now ${allInitiatedFlows.joinToString(", ", "[", "]") { it.description() }}.")
    }

    private fun FlowProcessor.description(): String = "'$id' version '$version'"
}