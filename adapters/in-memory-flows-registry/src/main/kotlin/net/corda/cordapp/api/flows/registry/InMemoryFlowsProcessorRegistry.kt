package net.corda.cordapp.api.flows.registry

import net.corda.commons.di.Plugin
import net.corda.commons.logging.loggerFor
import net.corda.node.api.cordapp.Cordapp
import net.corda.node.api.flows.registry.FlowsProcessorRegistry
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Plugin
@Named
class InMemoryFlowsProcessorRegistry @Inject constructor(private val logNewBinding: LogNewBinding) : FlowsProcessorRegistry {

    // TODO use the FlowProcessor interface here
    private val processorsByFlow: ConcurrentMap<String, MutableSet<Cordapp>> = ConcurrentHashMap()

    private companion object {

        private val logger = loggerFor<InMemoryFlowsProcessorRegistry>()
    }

    @PostConstruct
    private fun start() {

        // This still happens, regardless of the priority.
        logger.info("Initializing InMemoryFlowsProcessorRegistry")
    }

    // TODO use the FlowProcessor interface here
    override fun register(cordapp: Cordapp) {

        cordapp.allFlowsInitiating.forEach { flow ->

            val processors = processorsByFlow.computeIfAbsent(flow) { _ -> mutableSetOf() }
            processors += cordapp
            logNewBinding.apply(flow, cordapp, processors)
        }
    }

    override fun processorsForFlow(initiatingFlowName: String): Set<Cordapp> = processorsByFlow.getOrDefault(initiatingFlowName, emptySet<Cordapp>()).toSet()

    interface LogNewBinding {

        // TODO use the FlowProcessor interface here
        fun apply(initiating: String, processor: Cordapp, allInitiatedFlows: Set<Cordapp>)
    }
}