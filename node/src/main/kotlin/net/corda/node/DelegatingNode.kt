package net.corda.node

import net.corda.commons.events.EventSink
import net.corda.commons.events.EventSource
import net.corda.commons.events.EventSupport
import net.corda.commons.logging.loggerFor
import net.corda.node.api.Node
import net.corda.node.api.cordapp.Cordapp
import net.corda.node.api.cordapp.resolver.CordappsContainer
import net.corda.node.api.flows.processing.FlowProcessors
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Named

@Named
internal class DelegatingNode @Inject internal constructor(private val cordappsContainer: CordappsContainer, private val flowProcessors: FlowProcessors.Registry, private val configuration: Configuration, override val source: DelegatingNodeEventSupport = DelegatingNodeEventSupport()) : Node {

    companion object {
        private val logger = loggerFor<DelegatingNode>()
    }

    @PostConstruct
    override fun start() {

        with(configuration) {
            logger.info("Joining Corda network at address $networkHost:$networkPort.")
        }

        cordappsContainer.cordapps.forEach(::registerCordappAsFlowProcessor)

        source.publish(Node.Event.Initialisation.Completed())
    }

    @PreDestroy
    override fun stop() {

        source.close()
    }

    private fun registerCordappAsFlowProcessor(cordapp: Cordapp) {

        logger.info("Registering Cordapp ${cordapp.name} with version ${cordapp.version}, listening for flows ${cordapp.supportedFlowNames.joinToString(", ", "[", "]")}.")
        flowProcessors.register(cordapp)
    }

    interface Configuration {

        val networkHost: String
        val networkPort: Int
    }

    @Named
    internal class DelegatingNodeEventSupport : EventSupport<Node.Event>(), EventSource<Node.Event>, EventSink<Node.Event>
}