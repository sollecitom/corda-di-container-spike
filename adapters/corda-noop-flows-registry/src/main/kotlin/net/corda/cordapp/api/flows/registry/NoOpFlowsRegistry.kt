package net.corda.cordapp.api.flows.registry

import net.corda.cordapp.api.flows.Flows
import net.corda.node.api.flows.registry.FlowsRegistry
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

@Named
internal class NoOpFlowsRegistry @Inject constructor(private val logNewBinding: LogNewBinding) : FlowsRegistry {

    private val bindings: MutableMap<KClass<out Flows.Initiating<*>>, MutableSet<Flows.Initiated>> = mutableMapOf()

    override fun <INITIATING : Flows.Initiating<*>> register(initiating: KClass<out INITIATING>, initiated: Flows.Initiated) {

        val newSet = bindings.computeIfAbsent(initiating) { _ -> mutableSetOf() }
        newSet += initiated
        logNewBinding.apply(initiating, initiated, newSet)
    }
}

internal interface LogNewBinding {

    fun <INITIATING : Flows.Initiating<*>> apply(initiating: KClass<INITIATING>, initiated: Flows.Initiated, newSet: Set<Flows.Initiated>)
}