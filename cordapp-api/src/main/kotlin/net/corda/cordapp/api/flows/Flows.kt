package net.corda.cordapp.api.flows

import net.corda.commons.domain.network.Party
import kotlin.reflect.KClass

object Flows {

    interface Initiating<out RESULT : Any> {

        fun call(sessionManager: Flows.SessionManager): RESULT
    }

    interface Initiated {

        fun call(initiatingSession: Session)

        val initiatedBy: Set<KClass<out Initiating<*>>>
    }

    interface Session {

        fun send(message: Any)

        fun <MESSAGE : Any> receive(messageType: KClass<MESSAGE>): MESSAGE
    }

    interface SessionManager {

        fun initiateSession(party: Party): Session
    }
}

// Not advocating using Quasar with this. It is just to keep a similarity with current structure of Corda.
annotation class Suspendable

inline fun <reified MESSAGE : Any> Flows.Session.receive() = this.receive(MESSAGE::class)