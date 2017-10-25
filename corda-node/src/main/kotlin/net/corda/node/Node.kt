package net.corda.node

import net.corda.cordapps.resolver.CordAppsContainer
import net.corda.flows.registry.FlowsRegistry
import net.corda.logging.loggerFor
import javax.annotation.PostConstruct
import javax.inject.Named

@Named
internal class Node(private val cordAppsContainer: CordAppsContainer, private val flowsRegistry: FlowsRegistry, private val configuration: Configuration) {

    companion object {
        private val logger = loggerFor<Node>()
    }

    @PostConstruct
    private fun start() {

        with(configuration) {
            logger.info("Joining Corda network at address $networkHost:$networkPort.")
        }

        val cordApps = cordAppsContainer.cordApps
        cordApps.forEach { cordApp ->

            logger.info("Registering CordApp ${cordApp.name} with version ${cordApp.version}.")
            cordApp.initiatedFlows.forEach { initiated ->
                initiated.initiatedBy.forEach { initiating ->
                    flowsRegistry.register(initiating, initiated)
                }
            }
        }
    }

    interface Configuration {

        val networkHost: String
        val networkPort: Int
    }
}