package net.corda.cordapp.api.flows.registry

import net.corda.commons.di.Plugin
import net.corda.commons.logging.loggerFor
import net.corda.node.api.flows.processing.FlowProcessor
import net.corda.node.api.flows.processing.FlowProcessors
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Plugin
@Named
class InMemoryFlowsProcessorRegistry @Inject constructor(private val logNewBinding: LogNewBinding) : FlowProcessors.Registry, FlowProcessors.Repository {

    private val processorsByFlow: ConcurrentMap<String, MutableSet<FlowProcessor>> = ConcurrentHashMap()

    private companion object {

        private val logger = loggerFor<InMemoryFlowsProcessorRegistry>()
    }

    @PostConstruct
    private fun start() {

        // This still happens, regardless of the priority.
        logger.info("Initializing InMemoryFlowsProcessorRegistry")
    }

    override fun register(processor: FlowProcessor) {

        processor.supportedFlowNames.forEach { flow ->

            val processors = processorsByFlow.computeIfAbsent(flow) { _ -> mutableSetOf() }
            processors += processor
            logNewBinding.apply(flow, processor, processors)
        }
    }

    override fun forFlow(initiatingFlowName: String): Set<FlowProcessor> = processorsByFlow.getOrDefault(initiatingFlowName, emptySet<FlowProcessor>()).toSet()

    interface LogNewBinding {

        fun apply(initiating: String, processor: FlowProcessor, allInitiatedFlows: Set<FlowProcessor>)
    }
}