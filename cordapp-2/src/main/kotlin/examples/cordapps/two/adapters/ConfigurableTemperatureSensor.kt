package examples.cordapps.two.adapters

import examples.cordapps.one.domain.Temperature
import examples.cordapps.two.domain.Sensor
import javax.inject.Inject
import javax.inject.Named

@Named
internal class ConfigurableTemperatureSensor @Inject constructor(private val configuration: Configuration) : Sensor<Temperature> {

    override fun read() = configuration.value

    interface Configuration {

        val value: Temperature
    }
}