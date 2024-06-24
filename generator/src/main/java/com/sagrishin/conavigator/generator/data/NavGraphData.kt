package com.sagrishin.conavigator.generator.data

import com.sagrishin.conavigator.generator.utils.toStatement
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock

data class NavGraphData constructor(
    val installIntoGraphClassName: ClassName,
    val destinations: List<NavDestinationData>,
) {

    val implGraphClassName: ClassName
        get() = ClassName(installIntoGraphClassName.packageName, "${installIntoGraphClassName.simpleName}Impl")

    val baseRouteProvider: CodeBlock
        get() = "return %T.Companion.baseRoute".toStatement(installIntoGraphClassName)

    val startDestination: NavDestinationData
        get() = requireNotNull(destinations.find(NavDestinationData::isStartDestination)) {
            "There is no start destination for '${installIntoGraphClassName.simpleName}'"
        }

}
