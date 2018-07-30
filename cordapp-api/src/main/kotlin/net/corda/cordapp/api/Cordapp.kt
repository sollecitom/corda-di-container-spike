package net.corda.cordapp.api

import net.corda.cordapp.api.flows.Flows

interface Cordapp {

    val name: String
    val version: Int

    val initiatedFlows: Set<Flows.Initiated>
}