package net.corda.node

import net.corda.commons.events.PublishingEventSource
import net.corda.commons.utils.reactive.only
import net.corda.node.api.Node
import net.corda.node.api.cordapp.Cordapp
import net.corda.node.api.cordapp.CordappsLoader
import net.corda.node.api.flows.processing.FlowProcessors
import reactor.core.publisher.toFlux
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Named

private const val eventSourceQualifier = "DelegatingNodePublishingEventSource"

@Named
internal class DelegatingNode @Inject internal constructor(private val cordappsLoader: CordappsLoader, private val flowProcessors: FlowProcessors.Registry, @Named(eventSourceQualifier) override val source: PublishingEventSource<Node.Event> = DelegatingNodePublishingEventSource()) : Node {

    @PostConstruct
    override fun start() {

        cordappsLoader.events.only<CordappsLoader.Event.CordappsWereLoaded>().flatMap { it.loaded.toFlux() }.doOnNext(::registerCordappAsFlowProcessor).subscribe()

        source.publish(Node.Event.Initialisation.Completed())
    }

    @PreDestroy
    override fun stop() {

        source.close()
    }

    private fun registerCordappAsFlowProcessor(cordapp: Cordapp) {

        flowProcessors.register(cordapp)
    }

    @Named(eventSourceQualifier)
    private class DelegatingNodePublishingEventSource : PublishingEventSource<Node.Event>()
}