package net.corda.cordapps

import javax.inject.Named

const val NAME = "CORDAPP_1"
const val VERSION = 1
const val ROOT_PACKAGE = "examples.cordapps.one"

@Named
class CordApp1Descriptor : CordAppDescriptor {

    override val name = CordAppDescriptor.Name(NAME)

    override val version = CordAppDescriptor.Version(VERSION)

    override val rootPackage = CordAppDescriptor.Package(ROOT_PACKAGE)
}