package net.corda.cordapps

import javax.inject.Named

const val NAME = "CORDAPP_3"
const val VERSION = 1
const val ROOT_PACKAGE = "examples.cordapps.three"

@Named("CordApp3Descriptor_V1")
internal class DeclarativeCordAppDescriptor : CordAppDescriptor {

    override val name = CordAppDescriptor.Name(NAME)

    override val version = CordAppDescriptor.Version(VERSION)

    override val rootPackage = CordAppDescriptor.Package(ROOT_PACKAGE)
}