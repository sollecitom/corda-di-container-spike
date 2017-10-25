package net.corda.cordapps

import net.corda.flows.Flows

interface CordApp {

    val name: String
    val version: Int

    val initiatedFlows: Set<Flows.Initiated>
}