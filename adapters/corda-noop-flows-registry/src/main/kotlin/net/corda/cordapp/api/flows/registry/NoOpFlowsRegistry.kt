package net.corda.cordapp.api.flows.registry

import net.corda.cordapp.api.flows.Flows
import net.corda.node.api.flows.registry.FlowsRegistry
import javax.annotation.Priority
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

@Priority(Int.MAX_VALUE)
@Named
class NoOpFlowsRegistry @Inject constructor(private val logNewBinding: LogNewBinding) : FlowsRegistry {

    // TODO check for concurrent access
    private val bindings: MutableMap<KClass<out Flows.Initiating<*>>, MutableSet<Flows.Initiated>> = mutableMapOf()

    override fun <INITIATING : Flows.Initiating<*>> register(initiating: KClass<out INITIATING>, initiated: Flows.Initiated) {

        val initiatedFlows = bindings.computeIfAbsent(initiating) { _ -> mutableSetOf() }
        initiatedFlows += initiated
        logNewBinding.apply(initiating, initiated, initiatedFlows)
    }
}

interface LogNewBinding {

    fun <INITIATING : Flows.Initiating<*>> apply(initiating: KClass<INITIATING>, initiated: Flows.Initiated, allInitiatedFlows: Set<Flows.Initiated>)
}