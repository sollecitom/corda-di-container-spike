package net.corda.node.api.flows.processing

object FlowProcessors {

    interface Registry {

        fun register(processor: FlowProcessor)
    }

    interface Repository {

        fun forFlow(initiatingFlowName: String): Set<FlowProcessor>
    }
}