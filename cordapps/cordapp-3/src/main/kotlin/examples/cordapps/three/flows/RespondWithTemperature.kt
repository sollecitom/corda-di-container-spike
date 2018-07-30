package examples.cordapps.three.flows

import examples.cordapps.one.domain.Temperature
import examples.cordapps.one.flows.QueryClusterAverageTemperature
import net.corda.cordapp.api.flows.Flows
import net.corda.cordapp.api.flows.Suspendable
import javax.inject.Named

private const val VALUE = 10.0

@Named
internal class RespondWithTemperature : Flows.Initiated {

    @Suspendable
    override fun call(initiatingSession: Flows.Session) {

        initiatingSession.send(Temperature(VALUE))
    }

    override val initiatedBy = setOf(QueryClusterAverageTemperature::class)
}