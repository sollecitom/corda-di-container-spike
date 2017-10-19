package net.corda.flows.registry

import net.corda.flows.Flows
import kotlin.reflect.KClass

// TODO this should not be available to CordApps but there's no point in creating another module for the sake of this example.
interface FlowsRegistry {

    fun <INITIATING : Flows.Initiating<*>> register(initiating: KClass<out INITIATING>, initiated: Flows.Initiated)

    fun <INITIATING : Flows.Initiating<*>> register(initiating: KClass<out INITIATING>, initiatedSet: Set<Flows.Initiated>) = initiatedSet.forEach { register(initiating, it) }

    fun <INITIATING : Flows.Initiating<*>> register(initiating: KClass<out INITIATING>, initiated: Flows.Initiated, vararg additionalInitiated: Flows.Initiated) {

        register(initiating, initiated + setOf(*additionalInitiated))
    }

    private operator fun Flows.Initiated.plus(others: Set<Flows.Initiated>) = setOf(this) + others
}