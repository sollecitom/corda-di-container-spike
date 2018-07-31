package net.corda.node

import net.corda.commons.logging.loggerFor
import net.corda.node.api.cordapp.resolver.CordappsContainer
import net.corda.node.api.flows.registry.FlowsProcessorRegistry
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Named
internal class Node @Inject internal constructor(private val cordappsContainer: CordappsContainer, private val flowsProcessorRegistry: FlowsProcessorRegistry, private val configuration: Configuration) {

    companion object {
        private val logger = loggerFor<Node>()
    }

    @PostConstruct
    private fun start() {

        with(configuration) {
            logger.info("Joining Corda network at address $networkHost:$networkPort.")
        }

        // TODO subscribe to events instead
        val cordapps = cordappsContainer.cordapps()
        cordapps.forEach { cordapp ->

            logger.info("Registering Cordapp ${cordapp.name} with version ${cordapp.version}, listening for flows ${cordapp.allFlowsInitiating.joinToString(", ", "[", "]")}.")
            flowsProcessorRegistry.register(cordapp)
        }

        // TODO maybe show a lookup for flow processors
    }

    interface Configuration {

        val networkHost: String
        val networkPort: Int
    }
}