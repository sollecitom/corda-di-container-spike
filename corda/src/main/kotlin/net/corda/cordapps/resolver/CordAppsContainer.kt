package net.corda.cordapps.resolver

import net.corda.cordapps.CordAppDescriptor

// TODO this should not be available to CordApps but there's no point in creating another module for the sake of this example.
interface CordAppsContainer {

    val cordApps: Set<CordAppDescriptor>
}