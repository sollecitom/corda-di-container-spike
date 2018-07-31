package net.corda.cordapp.api.flows.registry

import net.corda.cordapp.api.flows.Flows
import net.corda.node.api.flows.registry.FlowsRegistry
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.annotation.Priority
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

@Priority(Int.MAX_VALUE)
@Named
class NoOpFlowsRegistry @Inject constructor(private val logNewBinding: LogNewBinding) : FlowsRegistry {

    private val bindings: ConcurrentMap<KClass<out Flows.Initiating<*>>, MutableSet<Flows.Initiated>> = ConcurrentHashMap()

    override fun <INITIATING : Flows.Initiating<*>> register(initiating: KClass<out INITIATING>, initiated: Flows.Initiated) {

        val initiatedFlows = bindings.computeIfAbsent(initiating) { _ -> mutableSetOf() }
        initiatedFlows += initiated
        logNewBinding.apply(initiating, initiated, initiatedFlows)
    }
}

interface LogNewBinding {

    fun <INITIATING : Flows.Initiating<*>> apply(initiating: KClass<INITIATING>, initiated: Flows.Initiated, allInitiatedFlows: Set<Flows.Initiated>)
}