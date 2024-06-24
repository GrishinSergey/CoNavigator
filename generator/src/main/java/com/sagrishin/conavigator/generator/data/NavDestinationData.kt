package com.sagrishin.conavigator.generator.data

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.sagrishin.conavigator.generator.utils.toNavRoute
import com.squareup.kotlinpoet.ClassName

data class NavDestinationData constructor(
    val annotatedTargetPackageName: String,
    val annotatedTargetName: String,
    val navArgs: ClassName?,
    val baseRoute: String = "",
    val graphInstallInto: ClassName?,
    val isStartDestination: Boolean,
    val requiresNavigator: Boolean,
    private val annotatedTarget: KSFunctionDeclaration,
) {
    val hasArguments: Boolean
        get() = navArgs != null

    val navRouteClassName: ClassName
        get() = annotatedTargetClassName.toNavRoute()

    val annotatedTargetClassName: ClassName
        get() = ClassName(annotatedTargetPackageName, annotatedTargetName)

    val destinationClassName: ClassName
        get() = ClassName(annotatedTargetPackageName, formattedDestinationName)

    val argsSerializer: ClassName?
        get() = navArgs?.let { ClassName(annotatedTargetPackageName, "${navArgs.simpleName}Serializer") }

    private val formattedDestinationName: String
        get() = "${annotatedTargetName}NavDestination"
}
