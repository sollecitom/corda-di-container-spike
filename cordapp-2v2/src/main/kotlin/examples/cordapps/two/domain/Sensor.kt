package examples.cordapps.two.domain

interface Sensor<out MEASUREMENT : Any> {

    fun read(): MEASUREMENT
}