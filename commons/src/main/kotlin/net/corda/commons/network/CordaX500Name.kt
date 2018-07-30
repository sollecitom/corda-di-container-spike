package net.corda.commons.network

data class CordaX500Name(val commonName: String?, val organisationUnit: String?, val organisation: String, val locality: String, val state: String?, val country: String) {

    constructor(commonName: String, organisation: String, locality: String, country: String) : this(commonName = commonName, organisationUnit = null, organisation = organisation, locality = locality, state = null, country = country)
}