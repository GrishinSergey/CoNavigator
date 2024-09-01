package com.sagrishin.conavigator.generator.utils

fun <K, V> Map<K, V>.reverseKeyValue(): Map<V, K> {
    return map { it }.associate { it.value to it.key }
}

suspend inline operator fun <reified T> SequenceScope<T>.plusAssign(t: T) {
    yield(t)
}
