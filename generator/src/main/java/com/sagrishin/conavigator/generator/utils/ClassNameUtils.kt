package com.sagrishin.conavigator.generator.utils

import com.squareup.kotlinpoet.ClassName

fun ClassName.toNavRoute(): ClassName {
    val name = buildList {
        addAll(simpleName.split("(?<=.)(?=\\p{Lu})".toRegex()).dropLast(1))
        add("Route")
    }

    return ClassName(packageName, name.joinToString(""))
}

fun ClassName.asNullable(): ClassName {
    return copy(nullable = true) as ClassName
}
