package net.corda.node

import com.natpryce.konfig.Key
import net.corda.cordapp.api.utils.Properties
import javax.inject.Named

private const val NETWORK_HOST = "config.node.network.host"
private const val NETWORK_PORT = "config.node.network.port"

private const val PROPERTIES = "/application.properties"

@Named
internal class NodeConfiguration : Properties(PROPERTIES, NodeConfiguration::class), Node.Configuration {

    companion object {
        private val hostKey = Key(NETWORK_HOST) { _, value -> value }
        private val portKey = Key(NETWORK_PORT) { _, value -> value.toInt() }
    }

    override val networkHost = get(hostKey)
    override val networkPort = get(portKey)
}