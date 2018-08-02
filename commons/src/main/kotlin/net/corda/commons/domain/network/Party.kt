package net.corda.commons.domain.network

// This should be in a network domain module, not in commons, but for the sake of keeping the spike contained it is here.
data class Party(val name: CordaX500Name)