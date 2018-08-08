package net.corda.cordapp.loading

import net.corda.commons.utils.logging.loggerFor
import net.corda.cordapp.api.flows.Flows
import net.corda.node.api.cordapp.Cordapp
import org.jboss.weld.environment.se.Weld

internal class RestrictedClassLoadingCordapp(override val name: String, override val version: Int, private val classLoader: ClassLoader) : Cordapp {
    private companion object {
        private val logger = loggerFor<RestrictedClassLoadingCordapp>()
    }

    private val initiatedFlows: Set<Flows.Initiated> by lazy {
        Weld.newInstance()
            .setClassLoader(classLoader)
            .initialize()
            .select(Flows.Initiated::class.java)
            .toSet()
    }

    override fun isInitiatedBy(initiatingFlowName: String): Boolean {
        return flowsInitiatedBy(initiatingFlowName).isNotEmpty()
    }

    override val supportedFlowNames: Set<String> by lazy {
        initiatedFlows.flatMap(Flows.Initiated::initiatedBy).map { it.java.name }.toSortedSet()
    }

    override fun process(flowName: String, payload: ByteArray) {
        logger.info("Cordapp '$name' version '$version' is processing flow '$flowName'.")
    }

    private fun flowsInitiatedBy(initiatingFlowName: String): Set<Flows.Initiated> = initiatedFlows.filter { flow -> flow.initiatedBy.any { it.qualifiedName == initiatingFlowName } }.toSet()

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        other as RestrictedClassLoadingCordapp
        return (name == other.name) && (version == other.version)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + version
        return result
    }

    override fun toString(): String {
        return "{name='$name', version=$version'}"
    }
}