package net.corda.commons.events

interface EventSink<in EVENT : Event> {

    fun publish(event: EVENT)
}