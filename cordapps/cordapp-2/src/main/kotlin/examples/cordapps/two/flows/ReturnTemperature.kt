package examples.cordapps.two.flows

import examples.cordapps.one.domain.Temperature
import examples.cordapps.one.flows.QueryClusterAverageTemperature
import examples.cordapps.two.domain.Sensor
import net.corda.cordapp.api.flows.Flows
import net.corda.cordapp.api.flows.Suspendable
import net.corda.cordapp.api.logging.loggerFor
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Named
internal class ReturnTemperature @Inject constructor(private val sensor: Sensor<Temperature>) : Flows.Initiated {

    companion object {
        private val logger = loggerFor<ReturnTemperature>()
    }

    @PostConstruct
    internal fun logInitialTemperature() {

        logger.info("Version 1 - Initial sensor temperature: ${sensor.read().value}.")
    }

    @Suspendable
    override fun call(session: Flows.Session, serviceHub: Flows.ServiceHub) {

        session.send(sensor.read())
    }

    override val initiatedBy = setOf(QueryClusterAverageTemperature::class)
}