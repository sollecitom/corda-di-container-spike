package examples.cordapps.two.adapters

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding

open class Properties(resourceFile: String) : Configuration by cascade(resourceFile)

private fun cascade(resourceFile: String) = systemProperties() overriding EnvironmentVariables() overriding ConfigurationProperties.fromResource(Properties::class.java, resourceFile)