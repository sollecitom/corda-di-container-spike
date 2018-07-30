package net.corda.cordapp.api.flows

import net.corda.cordapp.api.network.Party
import kotlin.reflect.KClass

object Flows {

    interface Initiating<out RESULT : Any> {

        fun call(serviceHub: ServiceHub): RESULT
    }

    interface Initiated {

        fun call(session: Session, serviceHub: ServiceHub)

        val initiatedBy: Set<KClass<out Initiating<*>>>
    }

    interface Session {

        fun send(message: Any)

        fun <MESSAGE : Any> receive(messageType: KClass<MESSAGE>): MESSAGE
    }

    interface ServiceHub {

        fun initiateSession(party: Party): Session
    }
}

annotation class Suspendable

inline fun <reified MESSAGE : Any> Flows.Session.receive() = this.receive(MESSAGE::class)