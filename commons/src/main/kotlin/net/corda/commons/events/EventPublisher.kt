package net.corda.commons.events

interface EventPublisher<EVENT : Event> {

    val source: EventSource<EVENT>

    val events get() = source.events
}