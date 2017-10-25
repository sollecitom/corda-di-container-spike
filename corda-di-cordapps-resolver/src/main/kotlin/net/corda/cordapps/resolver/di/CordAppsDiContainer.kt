package net.corda.cordapps.resolver.di

import net.corda.cordapps.CordApp
import net.corda.cordapps.resolver.CordAppsContainer
import net.corda.flows.Flows
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.io.File
import java.net.URLClassLoader
import java.util.jar.Attributes
import java.util.jar.JarInputStream
import java.util.jar.Manifest
import javax.inject.Named

@Named
internal class CordAppsDiContainer : CordAppsContainer {

    companion object {
        const val ROOT_PACKAGE_SEPARATOR = ";"
    }

    override val cordApps: Set<CordApp> by lazy {

        val libs = File("corda-node/libs")
        libs.walkTopDown().filter(this::isJar).map(this::toCordApp).toSet()
    }

    private fun toCordApp(jarFile: File): CordApp {

        return JarInputStream(jarFile.inputStream()).use { jar ->
            val manifest = jar.manifest
            val cordAppName = manifest["Implementation-Title"]
            val cordAppVersion = manifest["Implementation-Version"]?.toInt()
            val rootPackages = manifest["Root-Packages"]?.split(ROOT_PACKAGE_SEPARATOR)?.toSet() ?: throw IllegalArgumentException("CordApps should declare 1 or more root packages inside JAR manifest e.g., 'Root-Packages:examples.cordapps.one;com.apache.commons'!")
            if (cordAppName == null || cordAppVersion == null) {
                throw Exception("Invalid CordApp specification.")
            }
            CordaAppImpl(cordAppName, cordAppVersion, jarFile, rootPackages)
        }
    }

    private data class CordaAppImpl(override val name: String, override val version: Int, private val jarFile: File, private val packages: Set<String>) : CordApp {

        override val initiatedFlows: Set<Flows.Initiated> by lazy {

            // TODO here we have a classLoader per CordApp per version, along with a separate ApplicationContext.
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