package com.sagrishin.conavigator.generator

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.sagrishin.conavigator.generator.generators.NavDestinationGenerator
import com.sagrishin.conavigator.generator.generators.NavGraphGenerator

class NavDestinationSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return NavDestinationSymbolProcessor(
            navDestinationGenerator = NavDestinationGenerator(environment.codeGenerator, environment.logger),
            navGraphGenerator = NavGraphGenerator(environment.codeGenerator, environment.logger),
            logger = environment.logger,
        )
    }
}
