package com.sagrishin.conavigator.generator.utils

import com.squareup.kotlinpoet.ClassName

/* NavDestination args names */
const val NavGraphInstallIntoName = "installIn"
const val IsStartDestinationArgName = "isStart"
const val DestinationArgsVarName = "args"
const val BaseRouteTypeName = "baseRoute"

const val ContractsPackage = "com.sagrishin.conavigator.library"

val ComposableAnnotation = ClassName("androidx.compose.runtime", "Composable")
val NavBackStackEntryClassName = ClassName("androidx.navigation", "NavBackStackEntry")
val DestinationClassName = ClassName(ContractsPackage, "NavDestination")
val NavGraphClassName = ClassName(ContractsPackage, "NavGraph")
val NavRouteClassName = ClassName(ContractsPackage, "NavRoute")
val NavEntryClassName = ClassName(ContractsPackage, "NavEntry")
val NavArgumentsFactoryClassName = ClassName(ContractsPackage, "NavArguments", "Serializer")
val NavArgumentsClassName = ClassName(ContractsPackage, "NavArguments")
val NavigatorClassName = ClassName(ContractsPackage, "Navigator")
val AnimatedVisibilityScopeClassName = ClassName("androidx.compose.animation", "AnimatedVisibilityScope")
