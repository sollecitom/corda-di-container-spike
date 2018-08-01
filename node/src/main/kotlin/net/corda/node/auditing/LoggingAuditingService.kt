package net.corda.node.auditing

import net.corda.commons.events.Event
import net.corda.commons.logging.loggerFor
import net.corda.node.RpcServerStub
import net.corda.node.api.cordapp.CordappsLoader
import net.corda.node.api.events.EventBus
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

@Named
internal class LoggingAuditingService @Inject internal constructor(bus: EventBus) {

    private companion object {

        private val logger = loggerFor<LoggingAuditingService>()
        private val auditableEventTypes: Set<KClass<out Event>> = setOf(RpcServerStub.Event.Invocation::class, CordappsLoader.Event.CordappsWereLoaded::class)
    }

    init {
        bus.events.filter { event -> auditableEventTypes.any { it.isInstance(event) } }.doOnNext { event -> log(event) }.subscribe()
    }

    private fun log(event: Event) {

        when (event) {
            is RpcServerStub.Event.Invocation -> logRpcInvocation(event)
            is CordappsLoader.Event.CordappsWereLoaded -> logCordappsLoaded(event)
            else -> {
                logger.info("Event of type '${event.javaClass.name}' and ID '${event.id}' happened at ${LocalDateTime.ofInstant(event.createdAt, ZoneId.systemDefault())}.")
            }
        }
    }

    private fun logRpcInvocation(event: RpcServerStub.Event.Invocation) {

        logger.info("User '${event.user}' invoked flow '${event.flowName}' at ${LocalDateTime.ofInstant(event.createdAt, ZoneId.systemDefault())}. Event ID is '${event.id}'.")
    }

    private fun logCordappsLoaded(event: CordappsLoader.Event.CordappsWereLoaded) {

        logger.info("${event.loaded.size} Cordapps were loaded at ${LocalDateTime.ofInstant(event.createdAt, ZoneId.systemDefault())}. Event ID is '${event.id}'.")
    }
}