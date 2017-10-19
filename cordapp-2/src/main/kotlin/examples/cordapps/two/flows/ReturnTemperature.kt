package examples.cordapps.two.flows

import examples.cordapps.one.domain.Temperature
import examples.cordapps.one.flows.QueryClusterAverageTemperature
import examples.cordapps.two.domain.Sensor
import net.corda.flows.Flows
import net.corda.flows.Suspendable
import javax.inject.Inject
import javax.inject.Named

@Named
internal class ReturnTemperature @Inject constructor(private val sensor: Sensor<Temperature>) : Flows.Initiated {

    @Suspendable
    override fun call(session: Flows.Session, serviceHub: Flows.ServiceHub) {

        session.send(sensor.read())
    }

    override val initiatedBy = setOf(QueryClusterAverageTemperature::class)
}