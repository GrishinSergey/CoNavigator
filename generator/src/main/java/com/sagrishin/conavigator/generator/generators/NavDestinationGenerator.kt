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
            val argsSerializer = data.argsSerializer

            if (argsSerializer != null) {
                val navArgs = data.navArgs!!
                defineObject(argsSerializer) {
                    modifier(KModifier.PRIVATE)
                    superInterface(NavArgumentsFactoryClassName)

                    function("deserializeFrom", navArgs) {
                        modifier(KModifier.OVERRIDE)
                        param("string", String::class)

                        buildCodeBlock {
                            if (data.hasArguments) {
                                addImport(ContractsPackage, "decode")

                                returns { "string.decode<%T>()".formatWith(navArgs) }
                            } else {
                                returns { "null" }
                            }
                        }
                    }

                    function("serializeToString", String::class) {
                        modifier(KModifier.OVERRIDE)
                        param("args", NavArgumentsClassName)

                        buildCodeBlock {
                            addImport(ContractsPackage, "encode")
                            returns { "$DestinationArgsTypeName.encode()" }
                        }
                    }
                }
            }

            defineClass(data.navRouteClassName) {
                if (data.hasArguments) modifier(KModifier.DATA)
                superInterface(NavRouteClassName)
                constructor { if (data.navArgs != null) param(DestinationArgsTypeName, data.navArgs) }

                property(DestinationArgsTypeName, data.navArgs ?: NavArgumentsClassName.asNullable()) {
                    modifier(KModifier.OVERRIDE)
                    if (data.hasArguments) {
                        initializer(DestinationArgsTypeName)
                    } else {
                        getter { "return null" }
                    }
                }

                property("baseRoute", String::class) {
                    modifier(KModifier.OVERRIDE)
                    getter { "return \"${data.baseRoute}\"" }
                }

                function("computeRoute", String::class) {
                    modifier(KModifier.OVERRIDE)

                    buildCodeBlock {
                        if (data.hasArguments) {
                            returns {
                                val argsName = DestinationArgsTypeName
                                val format = "baseRoute.replace(\"{$argsName}\", %T.serializeToString($argsName))"
                                format.formatWith(argsSerializer!!)
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
                    getter { "return \"${data.baseRoute}\"" }
                }

                function("Composable") {
                    modifier(KModifier.OVERRIDE)
                    annotation(ComposableAnnotation)
                    params(
                        "entry" to NavBackStackEntryClassName,
                        "navigator" to NavigatorClassName,
                    )

                    buildCodeBlock {
                        val arguments = buildList {
                            if (data.hasArguments) add(DestinationArgsTypeName)
                            if (data.requiresNavigator) add("navigator")
                        }

                        if (data.hasArguments) {
                            addImport(ContractsPackage, "getNavArguments")

                            val format = "val args: %T = entry.getNavArguments(%T)"
                            addStatement(format, data.navArgs, data.argsSerializer)
                        }

                        addStatement("%T(${arguments.joinToString()})", data.annotatedTargetClassName)
                    }
                }
            }
        }.run {
//            logger.warn(toString())
            writeTo(codeGenerator, Dependencies.ALL_FILES)
        }
    }

}
