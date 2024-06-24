package com.sagrishin.conavigator.generator

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.sagrishin.conavigator.generator.annotation.Destination
import com.sagrishin.conavigator.generator.data.NavDestinationData
import com.sagrishin.conavigator.generator.data.NavGraphData
import com.sagrishin.conavigator.generator.generators.NavDestinationGenerator
import com.sagrishin.conavigator.generator.generators.NavGraphGenerator
import com.sagrishin.conavigator.generator.visitors.NavDestinationVisitor
import kotlin.reflect.KClass

class NavDestinationSymbolProcessor constructor(
    private val navDestinationGenerator: NavDestinationGenerator,
    private val navGraphGenerator: NavGraphGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        sequenceOf(Destination::class)
            .mapNotNull(KClass<out Annotation>::qualifiedName)
            .flatMap(resolver::getSymbolsWithAnnotation)
            .flatMap { it.accept(NavDestinationVisitor(), logger) }
            .onEach(navDestinationGenerator::generate)
            .groupBy(NavDestinationData::graphInstallInto)
            .asSequence()
            .filter { it.key != null }
            .map { NavGraphData(requireNotNull(it.key), it.value) }
            .forEach(navGraphGenerator::generate)

        return emptyList()
    }

}
