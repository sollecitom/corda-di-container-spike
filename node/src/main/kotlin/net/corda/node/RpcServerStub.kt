package net.corda.node

import net.corda.commons.events.filterIsInstance
import net.corda.commons.lifecycle.WithLifeCycle
import net.corda.commons.logging.loggerFor
import net.corda.node.api.Node
import net.corda.node.api.events.EventBus
import net.corda.node.api.flows.processing.FlowProcessors
import reactor.core.Disposable
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.inject.Inject
import javax.inject.Named

@Named
internal class RpcServerStub @Inject internal constructor(private val processors: FlowProcessors.Repository, private val bus: EventBus) : WithLifeCycle {

    private companion object {

        private val logger = loggerFor<RpcServerStub>()
        private const val queryTemperatureFlowFQN = "examples.cordapps.one.flows.QueryClusterAverageTemperature"
    }

    private var subscription: Disposable? = null

    @PostConstruct
    override fun start() {

        synchronized(this) {
            if (subscription == null) {
                subscription = bus.events.filterIsInstance<Node.Event.Initialisation.Completed>().doOnNext { _ -> logProcessorsForQueryTemperatureFlow() }.subscribe()
            }
        }
    }

    @PreDestroy
    override fun stop() {

        synchronized(this) {
            subscription?.dispose()
            subscription = null
        }
    }

    private fun logProcessorsForQueryTemperatureFlow() {

        logger.info("Flow '${RpcServerStub.queryTemperatureFlowFQN}' is supported by ${processors.forFlow(queryTemperatureFlowFQN).joinToString(", ", "[", "]") { processor -> "'${processor.id}' version '${processor.version}'" }}.")
    }
}