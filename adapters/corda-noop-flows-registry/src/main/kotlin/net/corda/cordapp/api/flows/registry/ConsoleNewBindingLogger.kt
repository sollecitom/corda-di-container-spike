package net.corda.cordapp.api.flows.registry

import net.corda.cordapp.api.flows.Flows
import net.corda.cordapp.api.logging.loggerFor
import javax.inject.Named
import kotlin.reflect.KClass

@Named
internal class ConsoleNewBindingLogger : LogNewBinding {

    companion object {
        private val logger = loggerFor<ConsoleNewBindingLogger>()
    }

    override fun <INITIATING : Flows.Initiating<*>> apply(initiating: KClass<INITIATING>, initiated: Flows.Initiated, newSet: Set<Flows.Initiated>) {

        logger.info("New binding $initiated registered for initiating flow ${initiating.qualifiedName}. Bindings are now $newSet.")
    }
}