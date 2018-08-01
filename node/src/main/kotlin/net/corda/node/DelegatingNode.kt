package net.corda.node

import net.corda.commons.events.PublishEvent
import net.corda.commons.logging.loggerFor
import net.corda.node.api.Node
import net.corda.node.api.cordapp.resolver.CordappsContainer
import net.corda.node.api.NodeEvents
import net.corda.node.api.flows.processing.FlowProcessors
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Named
internal class DelegatingNode @Inject internal constructor(private val cordappsContainer: CordappsContainer, private val flowProcessors: FlowProcessors.Registry, private val configuration: Configuration, private val publishEvent: PublishEvent) : Node {

    companion object {
        private val logger = loggerFor<DelegatingNode>()
    }

    @PostConstruct
    override fun start() {

        with(configuration) {
            logger.info("Joining Corda network at address $networkHost:$networkPort.")
        }

        // TODO subscribe to events instead
        val cordapps = cordappsContainer.cordapps()
        cordapps.forEach { cordapp ->

            logger.info("Registering Cordapp ${cordapp.name} with version ${cordapp.version}, listening for flows ${cordapp.supportedFlowNames.joinToString(", ", "[", "]")}.")
            flowProcessors.register(cordapp)
        }

        publishEvent(NodeEvents.Initialisation.Completed())
    }

    override fun stop() {

        // TODO call onComplete for the event stream
    }

    interface Configuration {

        val networkHost: String
        val networkPort: Int
    }
}