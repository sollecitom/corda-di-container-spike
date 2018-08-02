package net.corda.node.services

import net.corda.commons.domain.network.Party
import net.corda.cordapp.api.flows.Flows
import javax.inject.Named

@Named
internal class StubbedSessionManager : Flows.SessionManager {

    override fun initiateSession(party: Party): Flows.Session {

        throw NotImplementedError("Nothing interesting here!")
    }
}