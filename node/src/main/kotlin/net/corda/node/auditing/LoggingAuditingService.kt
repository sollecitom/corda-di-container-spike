package net.corda.node.auditing

import net.corda.commons.utils.logging.loggerFor
import net.corda.commons.utils.reactive.only
import net.corda.node.RpcServerStub
import net.corda.node.api.cordapp.CordappsLoader
import net.corda.node.api.events.EventBus
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named

@Named
internal class LoggingAuditingService @Inject internal constructor(bus: EventBus) {

    private companion object {

        private val logger = loggerFor<LoggingAuditingService>()
    }

    init {
        with(bus) {
            events.only<RpcServerStub.Event.Invocation>().doOnNext(::logRpcInvocation).subscribe()
            events.only<CordappsLoader.Event.CordappsWereLoaded>().doOnNext(::logCordappsLoaded).subscribe()
        }
    }

    private fun logRpcInvocation(event: RpcServerStub.Event.Invocation) {

        logger.info("User '${event.user}' invoked flow '${event.flowName}' at ${LocalDateTime.ofInstant(event.createdAt, ZoneId.systemDefault())}. Event ID is '${event.id}'.")
    }

    private fun logCordappsLoaded(event: CordappsLoader.Event.CordappsWereLoaded) {

        logger.info("${event.loaded.size} Cordapps were loaded at ${LocalDateTime.ofInstant(event.createdAt, ZoneId.systemDefault())}. Event ID is '${event.id}'.")
    }
}