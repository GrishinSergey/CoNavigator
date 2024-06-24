package com.sagrishin.conavigator.generator.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import kotlin.reflect.KClass

fun file(className: ClassName, builder: FileSpec.Builder.() -> Unit): FileSpec {
    return FileSpec.builder(className).apply(builder).build()
}

fun defineClass(name: ClassName, builder: TypeSpec.Builder.() -> Unit): TypeSpec {
    return TypeSpec.classBuilder(name).apply(builder).build()
}

fun FileSpec.Builder.defineClass(className: ClassName = ClassName(packageName, name), builder: TypeSpec.Builder.() -> Unit) {
    addType(defineClass(name = className, builder))
}

fun defineObject(name: ClassName, builder: TypeSpec.Builder.() -> Unit): TypeSpec {
    return TypeSpec.objectBuilder(name).apply(builder).build()
}

fun FileSpec.Builder.defineObject(className: ClassName = ClassName(packageName, name), builder: TypeSpec.Builder.() -> Unit) {
    addType(defineObject(name = className, builder))
}

fun TypeSpec.Builder.superInterface(className: ClassName) {
    addSuperinterface(className)
}

fun TypeSpec.Builder.constructor(builder: FunSpec.Builder.() -> Unit = {}): TypeSpec.Builder {
    return primaryConstructor(FunSpec.constructorBuilder().apply(builder).build())
}

fun <T : Any> TypeSpec.Builder.property(name: String, type: KClass<T>, builder: PropertySpec.Builder.() -> Unit = {}) {
    addProperty(PropertySpec.builder(name, type).apply(builder).build())
}

fun TypeSpec.Builder.property(name: String, type: TypeName, builder: PropertySpec.Builder.() -> Unit = {}) {
    addProperty(PropertySpec.builder(name, type).apply(builder).build())
}

fun TypeSpec.Builder.modifier(modifier: KModifier) {
    addModifiers(modifier)
}

fun PropertySpec.Builder.modifier(modifier: KModifier) {
    addModifiers(modifier)
}

fun PropertySpec.Builder.getter(builder: () -> String) {
    getter(codeBlock = buildCodeBlock { addStatement(builder()) })
}

fun PropertySpec.Builder.getter(codeBlock: CodeBlock) {
    getter(FunSpec.getterBuilder().addCode(codeBlock).build())
}

fun TypeSpec.Builder.function(name: String, builder: FunSpec.Builder.() -> CodeBlock) {
    function(name, Unit::class, builder)
}

fun <T : Any> TypeSpec.Builder.function(name: String, returns: KClass<T>, builder: FunSpec.Builder.() -> CodeBlock) {
    function(name, returns.asClassName(), builder)
}

fun TypeSpec.Builder.function(name: String, returns: ClassName, builder: FunSpec.Builder.() -> CodeBlock) {
    addFunction(FunSpec.builder(name).returns(returns).apply { addCode(builder()) }.build())
}

fun TypeSpec.Builder.companion(builder: TypeSpec.Builder.() -> Unit) {
    addType(TypeSpec.companionObjectBuilder().apply(builder).build())
}

fun String.formatWith(vararg params: Any): ParametrizedString {
    return ParametrizedString(this, params.toList())
}

data class ParametrizedString constructor(val format: String, val args: List<Any>): CharSequence by format

fun FunSpec.Builder.returns(builder: () -> CharSequence) {
    val built = builder()

    if (built is ParametrizedString) {
        val (format, params) = built
        addStatement("return $format", *params.toTypedArray())
    } else {
        addStatement("return $built")
    }
}

fun FunSpec.Builder.modifier(modifier: KModifier) {
    addModifiers(modifier)
}

fun FunSpec.Builder.annotation(className: ClassName) {
    addAnnotation(className)
}

fun <T: Any> FunSpec.Builder.param(name: String, type: KClass<T>, builder: ParameterSpec.Builder.() -> Unit = {}) {
    addParameter(ParameterSpec.builder(name, type).apply(builder).build())
}

fun FunSpec.Builder.param(name: String, type: ClassName, builder: ParameterSpec.Builder.() -> Unit = {}) {
    addParameter(ParameterSpec.builder(name, type).apply(builder).build())
}

fun <T: Any?> FunSpec.Builder.param(name: String, type: ClassName, defaultValue: T) {
    param(name, type) { defaultValue("$defaultValue") }
}

fun FunSpec.Builder.params(vararg map: Pair<String, ClassName>) {
    addParameters(map.map { (key, value) -> ParameterSpec.builder(key, value).build() })
}

fun String.toStatement(vararg arguments: Any): CodeBlock {
    return buildCodeBlock { addStatement(this@toStatement, args = arguments) }
}
