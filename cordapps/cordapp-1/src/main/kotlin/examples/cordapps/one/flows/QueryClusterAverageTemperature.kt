package examples.cordapps.one.flows

import examples.cordapps.one.domain.Temperature
import net.corda.cordapp.api.flows.Flows
import net.corda.cordapp.api.flows.Suspendable
import net.corda.cordapp.api.flows.receive
import net.corda.commons.domain.network.Party

class QueryClusterAverageTemperature(private val parties: Set<Party>) : Flows.Initiating<Temperature> {

    @Suspendable
    override fun call(sessionManager: Flows.SessionManager): Temperature {

        val total = parties.fold(Temperature(0.0)) { total, it ->
            val session = sessionManager.initiateSession(it)
            val temperature = session.receive<Temperature>()
            total + temperature
        }
        return total / parties.size
    }
}