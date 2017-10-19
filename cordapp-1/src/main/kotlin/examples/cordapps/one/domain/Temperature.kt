package examples.cordapps.one.domain

data class Temperature(val value: Double) {

    operator fun plus(other: Temperature) = Temperature(value + other.value)

    operator fun minus(other: Temperature) = Temperature(value - other.value)

    operator fun times(value: Number) = Temperature(this.value * value.toDouble())

    operator fun div(value: Number) = Temperature(this.value / value.toDouble())
}