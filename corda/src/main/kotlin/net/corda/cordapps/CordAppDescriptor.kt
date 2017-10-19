package net.corda.cordapps

// TODO this should not be available to CordApps but there's no point in creating another module for the sake of this example.
interface CordAppDescriptor {

    val name: Name

    val version: Version

    val rootPackage: Package

    data class Name(val value: String)

    data class Version(val value: Int)

    data class Package(val value: String)
}