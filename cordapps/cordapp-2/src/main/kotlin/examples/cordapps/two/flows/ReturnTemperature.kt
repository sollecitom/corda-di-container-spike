package examples.cordapps.two.flows

import examples.cordapps.one.domain.Temperature
import examples.cordapps.one.flows.QueryClusterAverageTemperature
import examples.cordapps.two.domain.Sensor
import net.corda.commons.domain.network.CordaX500Name
import net.corda.commons.domain.network.Party
import net.corda.cordapp.api.flows.Flows
import net.corda.cordapp.api.flows.Suspendable
import net.corda.commons.utils.logging.loggerFor
import javax.annotation.PostConstruct
import javax.enterprise.context.Dependent
import javax.inject.Inject

@Dependent
internal class ReturnTemperature @Inject constructor(private val sensor: Sensor<Temperature>, private val sessionManager: Flows.SessionManager) : Flows.Initiated {

    companion object {
        private val logger = loggerFor<ReturnTemperature>()
    }

    @PostConstruct
    private fun logInitialTemperature() {

        logger.info("Version 1 - Initial sensor temperature: ${sensor.read().value}.")
    }

    @Suspendable
    override fun call(initiatingSession: Flows.Session) {

        initiatingSession.send(sensor.read())
        val bruce = Party(CordaX500Name("Bruce", "Wayne Enterprises", "Gotham", "US"))

        val sessionWithBruce = sessionManager.initiateSession(bruce)
        // Nothing interesting to do with Bruce here, useful to showcase injection of Corda services into Cordapps though.
    }

    override val initiatedBy = setOf(QueryClusterAverageTemperature::class)
}