package net.corda.node.api.flows.processing

interface FlowProcessor {

    val id: String

    val version: Int

    val supportedFlowNames: Set<String>

    fun isInitiatedBy(initiatingFlowName: String): Boolean

    // Just an example, could be async, callback based, return something, etc.
    fun process(flowName: String, payload: ByteArray)
}