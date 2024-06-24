plugins {
    alias(libs.plugins.application.gradle.plugin)
    alias(libs.plugins.kotlin.android.gradle.plugin)
    alias(libs.plugins.ksp.devtools.gradle.plugin)
    alias(libs.plugins.kotlin.parcelize.gradle.plugin)
}

android {
    namespace = "com.sagrishin.conavigator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sagrishin.conavigator"

        minSdk = 24
        targetSdk = 34

        versionCode = 1
        versionName = "1.0"
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.version.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.library)
    implementation(projects.generator)
    ksp(projects.generator)

    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation)
}
