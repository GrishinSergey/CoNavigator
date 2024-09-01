package com.sagrishin.conavigator.generator.generators

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.sagrishin.conavigator.generator.data.NavDestinationData
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.writeTo
import com.sagrishin.conavigator.generator.utils.*

class NavDestinationGenerator constructor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : BaseGenerator<NavDestinationData>() {

    override fun generate(data: NavDestinationData) {
        file(data.destinationClassName) {
            val destinationArgsSerializerClassName = data.argsSerializer

            if (destinationArgsSerializerClassName != null) {
                val navArgs = data.navArgs!!
                defineObject(destinationArgsSerializerClassName) {
                    modifier(KModifier.PRIVATE)
                    superInterface(NavArgumentsFactoryClassName)

                    function("deserializeFrom", navArgs) {
                        modifier(KModifier.OVERRIDE)
                        param("string", String::class)

                        buildCodeBlock {
                            addImport(ContractsPackage, "decode")
                            returns { "string.decode<%T>()".formatWith(navArgs) }
                        }
                    }

                    function("serializeToString", String::class) {
                        modifier(KModifier.OVERRIDE)
                        param("args", NavArgumentsClassName)

                        buildCodeBlock {
                            addImport(ContractsPackage, "encode")
                            returns { "$DestinationArgsVarName.encode()" }
                        }
                    }
                }
            }

            defineClass(data.destinationRouteClassName) {
                if (data.hasArguments) modifier(KModifier.DATA)
                superInterface(NavRouteClassName)
                constructor { if (data.navArgs != null) param(DestinationArgsVarName, data.navArgs) }

                property(DestinationArgsVarName, data.navArgs ?: NavArgumentsClassName.asNullable()) {
                    modifier(KModifier.OVERRIDE)
                    if (data.hasArguments) {
                        initializer(DestinationArgsVarName)
                    } else {
                        getter { "null" }
                    }
                }

                property("baseRoute", String::class) {
                    modifier(KModifier.OVERRIDE)
                    getter { "\"${data.baseRoute}\"" }
                }

                function("computeRoute", String::class) {
                    modifier(KModifier.OVERRIDE)

                    buildCodeBlock {
                        if (data.hasArguments) {
                            returns {
                                val argsName = DestinationArgsVarName
                                val format = "baseRoute.replace(\"{$argsName}\", %T.serializeToString($argsName))"
                                format.formatWith(destinationArgsSerializerClassName!!)
                            }
                        } else {
                            returns { "baseRoute" }
                        }
                    }
                }
            }

            defineObject {
                superInterface(DestinationClassName)

                property("baseRoute", String::class) {
                    modifier(KModifier.OVERRIDE)
                    getter { "\"${data.baseRoute}\"" }
                }

                val composableParams = mapOf(
                    "entry" to NavBackStackEntryClassName,
                    "animatedScope" to AnimatedVisibilityScopeClassName,
                    "navigator" to NavigatorClassName,
                )
                function("Composable") {
                    modifier(KModifier.OVERRIDE)
                    annotation(ComposableAnnotation)
                    params(composableParams)

                    buildCodeBlock {
                        val reversed = composableParams.reverseKeyValue()
                        val arguments = data.arguments.map { (name, type) ->
                            name to if (type == data.navArgs) DestinationArgsVarName else reversed.getValue(type)
                        }

                        if (data.hasArguments) {
                            addImport(ContractsPackage, "getNavArguments")

                            val format = "val ${DestinationArgsVarName}: %T = entry.getNavArguments(%T)"
                            addStatement(format, data.navArgs, data.argsSerializer)
                        }

                        val params = arguments.joinToString { (name, varName) -> "$name = $varName" }
                        addStatement("%T($params)", data.annotatedTargetClassName)
                    }
                }
            }
        }.run {
//            logger.warn(toString())
            writeTo(codeGenerator, Dependencies.ALL_FILES)
        }
    }

}
