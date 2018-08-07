package net.corda

import net.corda.commons.utils.logging.loggerFor
import net.corda.node.DelegatingNode
import net.corda.node.RpcServerStub.Event
import org.jboss.weld.environment.se.Weld

class NodeStarter {
    private companion object {
        private val log = loggerFor<NodeStarter>()

        @JvmStatic
        fun main(args: Array<String>) {
            Weld.newInstance()
                .initialize()
                .use { container ->
                    container.beanManager.fireEvent(DelegatingNode.BootEvent())
                    log.info("RUNNING NODE")
                    container.beanManager.fireEvent(Event.Invocation("examples.cordapps.one.flows.QueryClusterAverageTemperature", "Bruce Wayne"))
                    log.info("Wait for it...")
                    Thread.sleep(5000)
                }
            log.info("Done.")
        }
    }
}