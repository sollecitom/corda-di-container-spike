package examples.cordapps.two.adapters

import com.natpryce.konfig.Key
import examples.cordapps.one.domain.Temperature
import net.corda.commons.utils.Properties
import javax.enterprise.context.Dependent

private const val PROPERTIES = "/cordapp2.properties"

@Dependent
internal class ResolvedConfiguration : Properties(PROPERTIES, ResolvedConfiguration::class), ConfigurableTemperatureSensor.Configuration {

    companion object {
        private val key = Key("config.sensors.temperature.value") { _, value -> Temperature(value.toDouble()) }
    }

    override val value: Temperature = get(key)
}