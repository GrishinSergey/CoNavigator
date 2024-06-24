package com.sagrishin.conavigator.generator.visitors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSDefaultVisitor

abstract class BaseVisitor<R> : KSDefaultVisitor<KSPLogger, Sequence<R>>() {
    override fun defaultHandler(node: KSNode, data: KSPLogger): Sequence<R> = emptySequence()

    open fun visitFunctionDeclaration(function: KSFunctionDeclaration): Sequence<R> {
        return emptySequence()
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: KSPLogger): Sequence<R> {
        return visitFunctionDeclaration(function)
    }
}
