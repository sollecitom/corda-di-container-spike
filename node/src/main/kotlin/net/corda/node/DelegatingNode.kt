package net.corda.node

import net.corda.commons.events.EventSupport
import net.corda.commons.events.only
import net.corda.commons.logging.loggerFor
import net.corda.node.api.Node
import net.corda.node.api.cordapp.Cordapp
import net.corda.node.api.cordapp.CordappsLoader
import net.corda.node.api.flows.processing.FlowProcessors
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Named

@Named
internal class DelegatingNode @Inject internal constructor(private val cordappsLoader: CordappsLoader, private val flowProcessors: FlowProcessors.Registry, private val configuration: Configuration, override val source: DelegatingNodeEventSupport = DelegatingNodeEventSupport()) : Node {

    companion object {
        private val logger = loggerFor<DelegatingNode>()
    }

    @PostConstruct
    override fun start() {

        with(configuration) {
            logger.info("Joining Corda network at address $networkHost:$networkPort.")
        }

        cordappsLoader.events.only<CordappsLoader.Event.CordappsWereLoaded>().doOnNext { event -> event.loaded.forEach(::registerCordappAsFlowProcessor) }.subscribe()

        source.publish(Node.Event.Initialisation.Completed())
    }

    @PreDestroy
    override fun stop() {

        source.close()
    }

    private fun registerCordappAsFlowProcessor(cordapp: Cordapp) {

        flowProcessors.register(cordapp)
    }

    interface Configuration {

        val networkHost: String
        val networkPort: Int
    }

    @Named
    internal class DelegatingNodeEventSupport : EventSupport<Node.Event>()
}