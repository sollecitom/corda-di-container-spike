package net.corda.node.api.cordapp

import net.corda.node.api.flows.processing.FlowProcessor

interface Cordapp : FlowProcessor, AutoCloseable {

    val name: String

    override val id get() = name
}