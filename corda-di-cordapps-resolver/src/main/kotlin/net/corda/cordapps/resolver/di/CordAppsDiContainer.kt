package net.corda.cordapps.resolver.di

import net.corda.cordapps.CordAppDescriptor
import net.corda.cordapps.resolver.CordAppsContainer
import javax.inject.Inject
import javax.inject.Named

// TODO this finds all instances of CordAppDescriptor in the DI context
@Named
internal class CordAppsDiContainer @Inject constructor(override val cordApps: Set<CordAppDescriptor>) : CordAppsContainer