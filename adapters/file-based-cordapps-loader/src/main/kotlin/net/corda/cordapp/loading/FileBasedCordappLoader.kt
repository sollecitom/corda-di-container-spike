package net.corda.cordapp.loading

import net.corda.commons.events.EventSupport
import net.corda.commons.utils.logging.loggerFor
import net.corda.node.api.cordapp.Cordapp
import net.corda.node.api.cordapp.CordappsLoader
import java.io.File
import java.net.URLClassLoader
import java.util.jar.Attributes
import java.util.jar.JarInputStream
import java.util.jar.Manifest
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
internal class FileBasedCordappLoader @Inject internal constructor(override val source: EventSupport<CordappsLoader.Event>) : CordappsLoader {

    private companion object {
        private val CORDAPP_AUGMENTING_PACKAGES = setOf(Package.getPackage("net.corda.node.services"))

        private val cordappsDirectory = File("node/cordapps")
        private val log = loggerFor<FileBasedCordappLoader>()
    }

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

    private fun toCordapp(jarFile: File): RestrictedClassLoadingCordapp {
        log.info(">> Creating CorDapp: $jarFile")

        val cordappClassLoader = URLClassLoader(arrayOf(jarFile.toURI().toURL()), javaClass.classLoader)
        return JarInputStream(jarFile.inputStream()).use { jar ->

            val manifest = jar.manifest
            val cordappName = manifest["Implementation-Title"]
            val cordappVersion = manifest["Implementation-Version"]?.toInt()
            if (cordappName == null || cordappVersion == null) {
                throw Exception("Invalid Cordapp specification.")
            }
            RestrictedClassLoadingCordapp(cordappName, cordappVersion, CORDAPP_AUGMENTING_PACKAGES, cordappClassLoader)
        }
    }

    private fun isJar(file: File) = !file.isDirectory && file.extension == "jar"

    private operator fun Manifest.get(key: String): String? = mainAttributes[Attributes.Name(key)]?.let { it as String }

    @ApplicationScoped
    internal class FileBasedCordappLoaderEventSupport : EventSupport<CordappsLoader.Event>()
}