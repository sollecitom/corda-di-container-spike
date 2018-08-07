package net.corda.node

import com.natpryce.konfig.Key
import net.corda.commons.utils.Properties
import javax.enterprise.context.Dependent

private const val NETWORK_HOST = "config.node.network.host"
private const val NETWORK_PORT = "config.node.network.port"

private const val PROPERTIES = "/application.properties"

@Dependent
internal class RpcServerConfiguration : Properties(PROPERTIES, RpcServerConfiguration::class), RpcServerStub.Configuration {

    companion object {
        private val hostKey = Key(NETWORK_HOST) { _, value -> value }
        private val portKey = Key(NETWORK_PORT) { _, value -> value.toInt() }
    }

    override val networkHost = get(hostKey)
    override val networkPort = get(portKey)
}