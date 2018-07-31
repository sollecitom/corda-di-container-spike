package net.corda.node.api.flows.registry

import net.corda.node.api.cordapp.Cordapp

interface FlowsProcessorRegistry {

    // TODO introduce FlowProcessor interface Cordapp implements
    fun processorsForFlow(initiatingFlowName: String): Set<Cordapp>

    // TODO introduce FlowProcessor interface Cordapp implements
    fun register(cordapp: Cordapp)
}