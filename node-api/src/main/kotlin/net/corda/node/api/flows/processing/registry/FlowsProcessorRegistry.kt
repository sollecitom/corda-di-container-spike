package net.corda.node.api.flows.processing.registry

import net.corda.node.api.flows.processing.FlowProcessor

// TODO split this into 2 interfaces, read-only and write-only
interface FlowsProcessorRegistry {

    // TODO add optional "version" here
    fun processorsForFlow(initiatingFlowName: String): Set<FlowProcessor>

    fun register(processor: FlowProcessor)
}