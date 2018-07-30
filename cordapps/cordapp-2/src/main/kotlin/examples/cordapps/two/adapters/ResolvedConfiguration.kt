package examples.cordapps.two.adapters

import com.natpryce.konfig.Key
import examples.cordapps.one.domain.Temperature
import net.corda.cordapp.api.utils.Properties
import javax.inject.Named

private const val PROPERTIES = "/cordapp2.properties"

@Named
internal class ResolvedConfiguration : Properties(PROPERTIES, ResolvedConfiguration::class), ConfigurableTemperatureSensor.Configuration {

    companion object {
        private val key = Key("config.sensors.temperature.value") { _, value -> Temperature(value.toDouble()) }
    }

    override val value: Temperature = get(key)
}