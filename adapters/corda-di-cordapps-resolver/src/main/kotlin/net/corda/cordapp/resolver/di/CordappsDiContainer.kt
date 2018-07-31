package net.corda.cordapp.resolver.di

import net.corda.cordapp.api.flows.Flows
import net.corda.node.api.cordapp.Cordapp
import net.corda.node.api.cordapp.resolver.CordappsContainer
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

        private val CORDAPP_AUGMENTING_PACKAGES = arrayOf("net.corda.node.services")
    }

    override fun cordapps(): Set<Cordapp> {

        val libs = File("node/cordapps")
        // TODO use a watcher to pick up events
        val cordapps = libs.walkTopDown().filter(this::isJar).map(this::toCordapp).toList()
        val distinct = cordapps.toSet()
        require(distinct.size == cordapps.size) { "Cordapps are not distinct. Found $cordapps." }
        return distinct
    }

    private fun toCordapp(jarFile: File): CordaAppImpl {

        return JarInputStream(jarFile.inputStream()).use { jar ->

            val manifest = jar.manifest
            val cordappName = manifest["Implementation-Title"]
            val cordappVersion = manifest["Implementation-Version"]?.toInt()
            val rootPackages = manifest["Root-Packages"]?.split(ROOT_PACKAGE_SEPARATOR)?.toSet()
                    ?: throw IllegalArgumentException("Cordapps should declare 1 or more root packages inside JAR manifest e.g., 'Root-Packages:examples.cordapp.one;com.apache.commons'!")
            if (cordappName == null || cordappVersion == null) {
                throw Exception("Invalid Cordapp specification.")
            }
            CordaAppImpl(cordappName, cordappVersion, jarFile, rootPackages + CORDAPP_AUGMENTING_PACKAGES, this.javaClass.classLoader)
        }
    }

    private class CordaAppImpl(override val name: String, override val version: Int, private val jarFile: File, private val rootPackages: Set<String>, parentClassLoader: ClassLoader) : Cordapp {

        private val classLoader = URLClassLoader(arrayOf(jarFile.toURI().toURL()), parentClassLoader)

        override val initiatedFlows: Set<Flows.Initiated> by lazy {

            // Here we have a classLoader per Cordapp per version, along with a separate ApplicationContext.
            val context = AnnotationConfigApplicationContext()
            context.classLoader = classLoader
            context.scan(*rootPackages.toTypedArray())
            context.refresh()
            context.getBeansOfType(Flows.Initiated::class.java).values.toSet()
        }

        override fun equals(other: Any?): Boolean {

            if (this === other) {
                return true
            }
            if (javaClass != other?.javaClass) {
                return false
            }

            other as CordaAppImpl

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

            return "{name='$name', version=$version, jarFile='${jarFile.toPath().toAbsolutePath()}'}"
        }


    }

    private fun isJar(file: File) = !file.isDirectory && file.extension == "jar"

    private operator fun Manifest.get(key: String): String? = this.mainAttributes[Attributes.Name(key)]?.let { it as String }
}