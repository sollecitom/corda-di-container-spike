package net.corda.node.api.cordapp.resolver

import net.corda.cordapp.api.Cordapp

interface CordappsContainer {

    fun cordapps(): Set<Cordapp>

    //    TODO add events for loading / unloading Cordapps
}