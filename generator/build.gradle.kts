plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm.gradle.plugin)
    alias(libs.plugins.ksp.devtools.gradle.plugin)
    id("maven-publish")
}

group = "com.sagrishin.conavigator.generator"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.kotlinpoet.library)
    implementation(libs.ksp.symbol.processing.api)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.sagrishin.conavigator"
            artifactId = "nav-generator"
            version = "1.0.0"

            from(components["java"])
        }
    }
}
