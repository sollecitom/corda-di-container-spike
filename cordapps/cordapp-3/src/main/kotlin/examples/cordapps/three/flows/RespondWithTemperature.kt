package examples.cordapps.three.flows

import examples.cordapps.one.domain.Temperature
import examples.cordapps.one.flows.QueryClusterAverageTemperature
import net.corda.cordapp.api.flows.Flows
import net.corda.cordapp.api.flows.Suspendable
import javax.enterprise.context.Dependent
import javax.inject.Inject

private const val VALUE = 10.0

@Dependent
internal class RespondWithTemperature @Inject constructor(private val sessionManager: Flows.SessionManager) : Flows.Initiated {

    @Suspendable
    override fun call(initiatingSession: Flows.Session) {

        initiatingSession.send(Temperature(VALUE))
    }

    override val initiatedBy = setOf(QueryClusterAverageTemperature::class)
}