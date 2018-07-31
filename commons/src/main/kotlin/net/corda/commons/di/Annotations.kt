package net.corda.commons.di

import javax.annotation.Priority

@Priority(0)
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Plugin

@Priority(Int.MAX_VALUE)
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultBehaviour