plugins {
    alias(libs.plugins.library.gradle.plugin)
    alias(libs.plugins.kotlin.android.gradle.plugin)
    id("maven-publish")
}

group = "com.sagrishin.conavigator.library"

android {
    namespace = "com.sagrishin.conavigator.library"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.version.get()
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.navigation.common.ktx)
    implementation(libs.navigation.runtime.ktx)
    implementation(libs.navigation.compose)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.sagrishin.conavigator"
            artifactId = "nav-library"
            version = "1.0.0"

            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
        }
    }
}
