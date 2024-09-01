package com.sagrishin.conavigator.generator.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Destination constructor(
    val isStart: Boolean = false,
    val installIn: KClass<out Any> = Any::class,
    val baseRoute: String = "",
)
