package net.corda.node

import org.springframework.beans.factory.annotation.Value
import javax.inject.Inject
import javax.inject.Named

const val NETWORK_HOST = "config.node.network.host"
const val NETWORK_PORT = "config.node.network.port"

// TODO property values injection requires annotations specific to Spring (@Value), and it won't work without a Spring-compatible DI Container.
@Named
internal class NodeConfiguration @Inject constructor(@Value("\${$NETWORK_HOST}") override val networkHost: String,
                                                    @Value("\${$NETWORK_PORT}") override val networkPort: Int) : Node.Configuration