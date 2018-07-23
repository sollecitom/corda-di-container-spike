package net.corda.cordapps.resolver

import net.corda.cordapps.Cordapp

// TODO this should not be available to Cordapps but there's no point in creating another module for the sake of this example.
interface CordappsContainer {

    fun cordapps(): Set<Cordapp>

    //    TODO add events for loading / unloading Cordapps
}