package net.corda.cordapp.loading

import net.corda.commons.events.EventSupport
import net.corda.node.api.cordapp.Cordapp
import net.corda.node.api.cordapp.CordappsLoader
import java.io.File
import java.util.jar.Attributes
import java.util.jar.JarInputStream
import java.util.jar.Manifest
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named

@Named
internal class FileBasedCordappLoader @Inject internal constructor(override val source: FileBasedCordappLoaderEventSupport = FileBasedCordappLoaderEventSupport()) : CordappsLoader {

    private companion object {
        private const val ROOT_PACKAGE_SEPARATOR = ";"

        private val CORDAPP_AUGMENTING_PACKAGES = arrayOf("net.corda.node.services")

        private val cordappsDirectory = File("node/cordapps")
    }

    // Spring needs this when the @Inject annotated constructor has only 1 parameter and this parameter has a default value (it conflicts with the default constructor).
    @Suppress("unused")
    private constructor() : this(source = FileBasedCordappLoaderEventSupport())

    override val cordapps: Set<Cordapp> by lazy {

        val cordapps = cordappsDirectory.walkTopDown().filter(this::isJar).map(this::toCordapp).toList()
        val distinct = cordapps.toSortedSet(Comparator.comparing(Cordapp::name).thenComparing(Cordapp::version))
        require(distinct.size == cordapps.size) { "Cordapps are not distinct. Found $cordapps." }
        distinct
    }

    @PostConstruct
    internal fun init() {

        source.publish(CordappsLoader.Event.CordappsWereLoaded(cordapps))
    }

    private fun toCordapp(jarFile: File): CordappImpl {

        return JarInputStream(jarFile.inputStream()).use { jar ->

            val manifest = jar.manifest
            val cordappName = manifest["Implementation-Title"]
            val cordappVersion = manifest["Implementation-Version"]?.toInt()
            val rootPackages = manifest["Root-Packages"]?.split(ROOT_PACKAGE_SEPARATOR)?.toSet()
                    ?: throw IllegalArgumentException("Cordapps should declare 1 or more root packages inside JAR manifest e.g., 'Root-Packages:examples.cordapp.one;com.apache.commons'!")
            if (cordappName == null || cordappVersion == null) {
                throw Exception("Invalid Cordapp specification.")
            }
            CordappImpl(cordappName, cordappVersion, jarFile, rootPackages + CORDAPP_AUGMENTING_PACKAGES, this.javaClass.classLoader)
        }
    }

    private fun isJar(file: File) = !file.isDirectory && file.extension == "jar"

    private operator fun Manifest.get(key: String): String? = this.mainAttributes[Attributes.Name(key)]?.let { it as String }

    @Named
    internal class FileBasedCordappLoaderEventSupport : EventSupport<CordappsLoader.Event>()
}