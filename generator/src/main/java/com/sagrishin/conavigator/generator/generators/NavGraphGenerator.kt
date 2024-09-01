package com.sagrishin.conavigator.generator.generators

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.sagrishin.conavigator.generator.data.NavDestinationData
import com.sagrishin.conavigator.generator.data.NavGraphData
import com.sagrishin.conavigator.generator.utils.DestinationArgsVarName
import com.sagrishin.conavigator.generator.utils.NavArgumentsClassName
import com.sagrishin.conavigator.generator.utils.NavEntryClassName
import com.sagrishin.conavigator.generator.utils.NavRouteClassName
import com.sagrishin.conavigator.generator.utils.asNullable
import com.sagrishin.conavigator.generator.utils.constructor
import com.sagrishin.conavigator.generator.utils.defineClass
import com.sagrishin.conavigator.generator.utils.file
import com.sagrishin.conavigator.generator.utils.function
import com.sagrishin.conavigator.generator.utils.getter
import com.sagrishin.conavigator.generator.utils.modifier
import com.sagrishin.conavigator.generator.utils.param
import com.sagrishin.conavigator.generator.utils.property
import com.sagrishin.conavigator.generator.utils.returns
import com.sagrishin.conavigator.generator.utils.superInterface
import com.sagrishin.conavigator.generator.utils.toNavRoute
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.writeTo

class NavGraphGenerator constructor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : BaseGenerator<NavGraphData>() {

    override fun generate(data: NavGraphData) {
        file(data.installIntoGraphClassName) {
            defineClass(data.installIntoGraphClassName.toNavRoute()) {
                superInterface(NavRouteClassName)
                constructor()

                property(DestinationArgsVarName, NavArgumentsClassName.asNullable()) {
                    modifier(KModifier.OVERRIDE)
                    getter { "null" }
                }

                property("baseRoute", String::class) {
                    modifier(KModifier.OVERRIDE)
                    getter(data.baseRouteProvider)
                }

                function("computeRoute", String::class) {
                    modifier(KModifier.OVERRIDE)
                    buildCodeBlock { returns { "baseRoute" } }
                }
            }

            defineClass(data.implGraphClassName) {
                superInterface(data.installIntoGraphClassName)

                constructor {
                    param("startRoute", NavRouteClassName)
                }

                property("startRoute", NavRouteClassName) {
                    modifier(KModifier.OVERRIDE)
                    initializer("startRoute")
                }

                property("baseRoute", String::class) {
                    modifier(KModifier.OVERRIDE)
                    getter(data.baseRouteProvider)
                }

                property("entries", Set::class.asClassName().parameterizedBy(NavEntryClassName)) {
                    modifier(KModifier.OVERRIDE)

                    initializer(codeBlock = buildCodeBlock {
                        val (formatted, params) = data.destinations.map(NavDestinationData::destinationClassName).let {
                            it.joinToString(",\n", "\n", ",\n") { "%T" } to it.toTypedArray()
                        }
                        addStatement("setOf(${formatted})", args = params)
                    })
                }
            }
        }.run {
//            logger.warn(toString())
            writeTo(codeGenerator, Dependencies.ALL_FILES)
        }
    }

}
