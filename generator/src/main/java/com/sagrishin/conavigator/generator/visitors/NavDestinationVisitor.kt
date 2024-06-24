package com.sagrishin.conavigator.generator.visitors

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter
import com.sagrishin.conavigator.generator.annotation.Destination
import com.sagrishin.conavigator.generator.data.NavDestinationData
import com.sagrishin.conavigator.generator.utils.BaseRouteTypeName
import com.sagrishin.conavigator.generator.utils.DestinationArgsTypeName
import com.sagrishin.conavigator.generator.utils.IsStartDestinationArgName
import com.sagrishin.conavigator.generator.utils.NavGraphInstallIntoName
import com.sagrishin.conavigator.generator.utils.NavigatorClassName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName

class NavDestinationVisitor : BaseVisitor<NavDestinationData>() {

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration): Sequence<NavDestinationData> {
        val annotation = function.findAnnotation<Destination>()
        val (packageName, simpleName) = (function.packageName.asString() to function.simpleName.asString())
        val navArgs = obtainNavArgs(annotation)
        val baseRoute = obtainBaseRoute(annotation, simpleName, navArgs != null)
        val graphInstallInto = obtainGraphInstallInto(annotation)
        val isStartDestination = isStartDestination(annotation)
        val requiresNavigator = findNavigatorInParamsOf(function)?.name != null

        return sequenceOf(
            NavDestinationData(
                annotatedTarget = function,
                annotatedTargetPackageName = packageName,
                annotatedTargetName = simpleName,
                navArgs = navArgs,
                baseRoute = baseRoute,
                graphInstallInto = graphInstallInto,
                isStartDestination = isStartDestination,
                requiresNavigator = requiresNavigator,
            )
        )
    }

    private inline fun <reified A : Annotation> KSAnnotated.findAnnotation(): KSAnnotation {
        val simpleName = A::class.java.simpleName
        return annotations.first { it.shortName.asString() == simpleName }
    }

    private fun findNavigatorInParamsOf(function: KSFunctionDeclaration): KSValueParameter? {
        return function.parameters.find { it.type.resolve().toClassName() == NavigatorClassName }
    }

    private fun obtainBaseRoute(annotation: KSAnnotation, annotatedTargetName: String, hasArguments: Boolean): String {
        val baseRoute = annotation.findArgument<String>(BaseRouteTypeName)
        val routeParts = buildList {
            when (baseRoute.isNotBlank()) {
                true -> add(baseRoute)
                false -> addAll(annotatedTargetName.split("(?<=.)(?=\\p{Lu})".toRegex()).dropLast(1))
            }

            if (hasArguments) add("{${DestinationArgsTypeName}}")
        }

        return routeParts.joinToString("/").let { if (!it.startsWith("/")) "/$it" else it }.lowercase()
    }

    private fun obtainNavArgs(annotation: KSAnnotation): ClassName? {
        return annotation.findArgument<KSType?>(DestinationArgsTypeName)?.toClassName()
            .thisOrNullIf { it != Any::class.asClassName() }
    }

    private fun obtainGraphInstallInto(annotation: KSAnnotation): ClassName? {
        return annotation.findArgument<KSType?>(NavGraphInstallIntoName)?.toClassName()
            .thisOrNullIf { it != Any::class.asClassName() }
    }

    private fun isStartDestination(annotation: KSAnnotation): Boolean {
        return annotation.findArgument<Boolean>(IsStartDestinationArgName)
    }

    private inline fun <reified T : Any?> KSAnnotation.findArgument(name: String): T {
        val value = arguments.firstOrNull { it.name?.asString() == name }?.value.let { it as T }
        return if (null is T) value else requireNotNull(value)
    }

    private inline fun <reified T> T.thisOrNullIf(condition: (T) -> Boolean): T? {
        return if (condition(this)) this else null
    }

}
