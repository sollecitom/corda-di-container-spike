package net.corda.node.services

import net.corda.commons.domain.network.Party
import net.corda.commons.utils.logging.loggerFor
import net.corda.cordapp.api.flows.Flows
import javax.annotation.PostConstruct
import javax.inject.Named

@Named
internal class StubbedSessionManager : Flows.SessionManager {

    private companion object {
        private val logger = loggerFor<StubbedSessionManager>()
    }

    @PostConstruct
    internal fun logInit() {

        // This happens only once, as in the same instance gets injected into all Cordapps.
        logger.info("Initialising StubbedSessionManager $this.")
    }

    override fun initiateSession(party: Party): Flows.Session {

        throw NotImplementedError("Nothing interesting here!")
    }
}