package net.corda.node

import net.corda.cordapps.resolver.CordAppsContainer
import net.corda.flows.Flows
import net.corda.flows.registry.FlowsRegistry
import net.corda.logging.loggerFor
import org.springframework.context.annotation.AnnotationConfigApplicationContext
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
            logger.info("Registering CordApp ${cordApp.name.value} with version ${cordApp.version.value}. Scanning package \"${cordApp.rootPackage.value}\".")
            // TODO one separate DI context for each CordApp, meaning there's no risk to inject something from another CordApp by mistake.
            val context = AnnotationConfigApplicationContext(cordApp.rootPackage.value)
            val initiatedFlows = context.getBeansOfType(Flows.Initiated::class.java)
            initiatedFlows.map { it.value }.forEach { initiated ->
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