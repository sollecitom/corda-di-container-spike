package net.corda

import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
// TODO remove ComponentScan and move it to SpringBootApplication
@ComponentScan("net.corda")
private open class NodeStarter

internal fun main(args: Array<String>) {

    val application = SpringApplication(NodeStarter::class.java)

    application.setBannerMode(Banner.Mode.OFF)
    application.webApplicationType = WebApplicationType.NONE

    application.run(*args)
}