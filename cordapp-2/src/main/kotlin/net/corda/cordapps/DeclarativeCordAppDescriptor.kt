package net.corda.cordapps

import javax.inject.Named

const val NAME = "CORDAPP_2"
const val VERSION = 1
const val ROOT_PACKAGE = "examples.cordapps.two"

@Named("CordApp2Descriptor_V1")
internal class DeclarativeCordAppDescriptor : CordAppDescriptor {

    override val name = CordAppDescriptor.Name(NAME)

    override val version = CordAppDescriptor.Version(VERSION)

    override val rootPackage = CordAppDescriptor.Package(ROOT_PACKAGE)
}