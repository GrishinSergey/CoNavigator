plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm.gradle.plugin)
    alias(libs.plugins.ksp.devtools.gradle.plugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.kotlinpoet.library)
    implementation(libs.ksp.symbol.processing.api)
}
