package examples.cordapps.two.flows

import examples.cordapps.one.domain.Temperature
import examples.cordapps.one.flows.QueryClusterAverageTemperature
import examples.cordapps.two.domain.Sensor
import net.corda.cordapp.api.flows.Flows
import net.corda.cordapp.api.flows.Suspendable
import net.corda.commons.utils.logging.loggerFor
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Named
internal class ReturnTemperature @Inject constructor(private val sensor: Sensor<Temperature>) : Flows.Initiated {

    companion object {
        private val logger = loggerFor<ReturnTemperature>()
    }

    @PostConstruct
    private fun logInitialTemperature() {

        logger.info("Version 2 - Initial sensor temperature: ${sensor.read().value}.")
    }

    @Suspendable
    override fun call(initiatingSession: Flows.Session) {

        initiatingSession.send(sensor.read())
    }

    override val initiatedBy = setOf(QueryClusterAverageTemperature::class)
}