package net.corda.node.services

import net.corda.commons.domain.network.Party
import net.corda.commons.utils.logging.loggerFor
import net.corda.cordapp.api.flows.Flows
import net.corda.node.DelegatingNode
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
internal class StubbedSessionManager : Flows.SessionManager {
    private companion object {
        private val log = loggerFor<StubbedSessionManager>()
    }

    fun bootup(@Observes evt: DelegatingNode.BootEvent) {
        log.info("BOOTED: {}", this)
    }

    override fun initiateSession(party: Party): Flows.Session {

        throw NotImplementedError("Nothing interesting here!")
    }
}