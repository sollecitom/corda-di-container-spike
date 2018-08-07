package net.corda.node

import net.corda.commons.events.EventSupport
import net.corda.commons.utils.logging.loggerFor
import net.corda.commons.utils.reactive.only
import net.corda.node.api.Node
import net.corda.node.api.cordapp.Cordapp
import net.corda.node.api.cordapp.CordappsLoader
import net.corda.node.api.flows.processing.FlowProcessors
import reactor.core.publisher.toFlux
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject

@ApplicationScoped
internal class DelegatingNode @Inject internal constructor(
    private val cordappsLoader: CordappsLoader,
    private val flowProcessors: FlowProcessors.Registry,
    override val source: EventSupport<Node.Event>
) : Node {
    private companion object {
        private val log = loggerFor<DelegatingNode>()
    }

    fun bootup(@Observes startEvt: BootEvent) {
        log.info("BOOTED: {}", this)
    }

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
        log.info(">> Registering: {}", cordapp.id)
        flowProcessors.register(cordapp)
    }

    @ApplicationScoped
    internal class DelegatingNodeEventSupport : EventSupport<Node.Event>()

    class BootEvent
}