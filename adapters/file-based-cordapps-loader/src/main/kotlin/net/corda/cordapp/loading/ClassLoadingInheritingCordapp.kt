package net.corda.cordapp.loading

import net.corda.commons.utils.logging.loggerFor
import net.corda.cordapp.api.flows.Flows
import net.corda.node.api.cordapp.Cordapp
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

internal class ClassLoadingInheritingCordapp(override val name: String, override val version: Int, private val rootPackages: Set<String>, private val classLoader: ClassLoader, private val parentApplicationContext: ApplicationContext) : Cordapp {

    private companion object {

        private val logger = loggerFor<ClassLoadingInheritingCordapp>()
    }

    private val initiatedFlows: Set<Flows.Initiated> by lazy {

        // Here we have a classLoader per Cordapp per version, along with a separate ApplicationContext.
        val context = AnnotationConfigApplicationContext()
        context.parent = parentApplicationContext
        context.classLoader = classLoader
        context.scan(*rootPackages.toTypedArray())
        context.refresh()
        context.getBeansOfType(Flows.Initiated::class.java).values.toSet()
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

    private fun flowsInitiatedBy(initiatingFlowName: String): Set<Flows.Initiated> = initiatedFlows.filter { initiated -> initiated.initiatedBy.any { initiating -> initiating.qualifiedName == initiatingFlowName } }.toSet()

    override fun equals(other: Any?): Boolean {

        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        other as ClassLoadingInheritingCordapp

        if (name != other.name) {
            return false
        }
        if (version != other.version) {
            return false
        }

        return true
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