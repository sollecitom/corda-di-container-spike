package net.corda.node

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("net.corda")
private open class NodeStarter

internal fun main(args: Array<String>) {

    val application = SpringApplication(NodeStarter::class.java)

    application.setBannerMode(Banner.Mode.OFF)
    application.isWebEnvironment = false

    application.run(*args)
}