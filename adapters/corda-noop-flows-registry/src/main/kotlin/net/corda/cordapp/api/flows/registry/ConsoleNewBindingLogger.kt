package net.corda.cordapp.api.flows.registry

import net.corda.commons.logging.loggerFor
import net.corda.cordapp.api.flows.Flows
import javax.inject.Named
import kotlin.reflect.KClass

@Named
internal class ConsoleNewBindingLogger : LogNewBinding {

    private companion object {

        private val logger = loggerFor<ConsoleNewBindingLogger>()
    }

    override fun <INITIATING : Flows.Initiating<*>> apply(initiating: KClass<INITIATING>, initiated: Flows.Initiated, allInitiatedFlows: Set<Flows.Initiated>) {

        logger.info("New binding $initiated registered for initiating flow ${initiating.qualifiedName}. Bindings are now $allInitiatedFlows.")
    }
}