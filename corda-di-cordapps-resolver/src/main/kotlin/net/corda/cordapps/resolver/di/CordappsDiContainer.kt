package net.corda.cordapps.resolver.di

import net.corda.cordapps.Cordapp
import net.corda.cordapps.resolver.CordappsContainer
import net.corda.flows.Flows
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.io.File
import java.net.URLClassLoader
import java.util.jar.Attributes
import java.util.jar.JarInputStream
import java.util.jar.Manifest
import javax.inject.Named

@Named
internal class CordappsDiContainer : CordappsContainer {

    companion object {
        const val ROOT_PACKAGE_SEPARATOR = ";"
    }

    override val cordapps: Set<Cordapp> by lazy {

        val libs = File("corda-node/libs")
        libs.walkTopDown().filter(this::isJar).map(this::toCordapp).toSet()
    }

    private fun toCordapp(jarFile: File): Cordapp {

        return JarInputStream(jarFile.inputStream()).use { jar ->
            val manifest = jar.manifest
            val cordAppName = manifest["Implementation-Title"]
            val cordAppVersion = manifest["Implementation-Version"]?.toInt()
            val rootPackages = manifest["Root-Packages"]?.split(ROOT_PACKAGE_SEPARATOR)?.toSet() ?: throw IllegalArgumentException("Cordapps should declare 1 or more root packages inside JAR manifest e.g., 'Root-Packages:examples.cordapps.one;com.apache.commons'!")
            if (cordAppName == null || cordAppVersion == null) {
                throw Exception("Invalid Cordapp specification.")
            }
            CordaAppImpl(cordAppName, cordAppVersion, jarFile, rootPackages)
        }
    }

    private data class CordaAppImpl(override val name: String, override val version: Int, private val jarFile: File, private val packages: Set<String>) : Cordapp {

        override val initiatedFlows: Set<Flows.Initiated> by lazy {

            // Here we have a classLoader per Cordapp per version, along with a separate ApplicationContext.
            val context = AnnotationConfigApplicationContext()
            context.classLoader = classLoader
            context.scan(*packages.toTypedArray())
            context.refresh()
            context.getBeansOfType(Flows.Initiated::class.java).values.toSet()
        }

        private val classLoader: ClassLoader by lazy {

            URLClassLoader(arrayOf(jarFile.toURI().toURL()))
        }
    }

    private fun isJar(file: File) = !file.isDirectory && file.extension == "jar"

    private operator fun Manifest.get(key: String): String? = this.mainAttributes[Attributes.Name(key)]?.let { it as String }
}