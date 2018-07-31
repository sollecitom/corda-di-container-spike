package net.corda.node.adapters

import net.corda.commons.di.DefaultBehaviour
import net.corda.commons.logging.loggerFor
import net.corda.cordapp.api.flows.Flows
import net.corda.node.api.flows.registry.FlowsRegistry
import javax.annotation.PostConstruct
import javax.inject.Named
import kotlin.reflect.KClass

@DefaultBehaviour
@Named
class NopeFlowsRegistry : FlowsRegistry {

    private companion object {

        private val logger = loggerFor<NopeFlowsRegistry>()
    }

    @PostConstruct
    private fun start() {

        // This still happens, regardless of the priority
        logger.info("Initializing NopeFlowsRegistry")
    }

    override fun <INITIATING : Flows.Initiating<*>> register(initiating: KClass<out INITIATING>, initiated: Flows.Initiated) {

        logger.info("Nope!")
    }
}