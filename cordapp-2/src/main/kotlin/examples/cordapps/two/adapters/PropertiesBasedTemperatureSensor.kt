package examples.cordapps.two.adapters

import examples.cordapps.one.domain.Temperature
import examples.cordapps.two.domain.Sensor
import org.springframework.beans.factory.annotation.Value
import javax.inject.Inject
import javax.inject.Named

const val TEMPERATURE = "config.sensors.temperature.value"

// TODO property values injection requires annotations specific to Spring (@Value), and it won't work without a Spring-compatible DI Container.
@Named
internal class PropertiesBasedTemperatureSensor @Inject constructor(@Value("\${$TEMPERATURE}") rawValue: Double) : Sensor<Temperature> {

    private val value = Temperature(rawValue)

    override fun read() = value
}