package net.corda.node.api.cordapp

interface Cordapp {

    val name: String
    val version: Int

    fun isInitiatedBy(initiatingFlowName: String): Boolean

    val allFlowsInitiating: Set<String>
}