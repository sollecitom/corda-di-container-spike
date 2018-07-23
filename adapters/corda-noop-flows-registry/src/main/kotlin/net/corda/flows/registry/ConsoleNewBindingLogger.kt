package net.corda.flows.registry

import net.corda.flows.Flows
import net.corda.logging.loggerFor
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