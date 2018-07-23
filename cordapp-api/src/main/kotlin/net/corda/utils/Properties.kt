package net.corda.utils

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding
import kotlin.reflect.KClass

open class Properties(resourceFile: String, type: KClass<*> = Properties::class) : Configuration by cascade(resourceFile, type)

private fun cascade(resourceFile: String, type: KClass<*>) = systemProperties() overriding EnvironmentVariables() overriding ConfigurationProperties.fromResource(type.java, resourceFile)