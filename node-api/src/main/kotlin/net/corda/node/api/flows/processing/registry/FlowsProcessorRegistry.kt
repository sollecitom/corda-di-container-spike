package net.corda.node.api.flows.processing.registry

import net.corda.node.api.flows.processing.FlowProcessor

interface FlowsProcessorRegistry {

    fun processorsForFlow(initiatingFlowName: String): Set<FlowProcessor>

    fun register(processor: FlowProcessor)
}