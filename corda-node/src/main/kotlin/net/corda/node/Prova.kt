package net.corda.node

import net.corda.flows.Flows
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.io.File
import java.net.URLClassLoader
import java.util.jar.Attributes
import java.util.jar.JarInputStream
import java.util.jar.Manifest

fun main(args: Array<String>) {

    val libs = File("corda-node/libs")

    libs.walkTopDown().filter(::isJar).map(::toCordApp).forEach { cordApp ->
        println(cordApp)
//        val clazz = cordApp.classLoader.loadClass("net.corda.cordapps.DeclarativeCordAppDescriptor")

        val context = AnnotationConfigApplicationContext()
        context.classLoader = cordApp.classLoader
        context.scan("examples.cordapps")
        // TODO create a classloader with children
        Thread.currentThread().contextClassLoader = context.classLoader
        context.refresh()

        val initiatedFlows = context.getBeansOfType(Flows.Initiated::class.java)

        println(initiatedFlows)
    }
}

// TODO add CordApp set of dependencies
data class CordApp(val name: String, val version: Int, private val jarFile: File) {

    val classLoader: ClassLoader by lazy {
        URLClassLoader(arrayOf(jarFile.toURI().toURL()))
    }
}

private fun toCordApp(jarFile: File): CordApp {

    return JarInputStream(jarFile.inputStream()).use { jar ->
        val manifest = jar.manifest
        val cordAppName = manifest["Implementation-Title"]
        val cordAppVersion = manifest["Implementation-Version"]?.toInt()
        if (cordAppName == null || cordAppVersion == null) {
            throw Exception("Invalid CordApp specification.")
        }
        CordApp(cordAppName, cordAppVersion, jarFile)
    }
}

private fun isJar(file: File) = !file.isDirectory && file.extension == "jar" && file.parentFile?.name != "lib"

private operator fun Manifest.get(key: String): String? = this.mainAttributes[Attributes.Name(key)]?.let { it as String }