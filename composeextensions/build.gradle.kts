import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "sco.carlukesoftware.composeextensions"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xcontext-sensitive-resolution")
            jvmTarget = JvmTarget.fromTarget(libs.versions.jvmTarget.get())
        }
    }

    buildFeatures {
        compose = true

        // Disable unused AGP features
        viewBinding = false
        dataBinding = false
    }

    sourceSets["main"].java {
        srcDirs(
            "src/main/kotlin",
            "src/common/kotlin",
            "src/debug/kotlin",
            "src/release/kotlin",
            "src/staging/kotlin",
            "src/preproduction/kotlin"
        )
    }

    // Add this to your android block to generate Javadoc from Kotlin sources
    // (if you are using Dokka, the setup is different)
    // For basic Javadoc with KDoc:
    tasks.withType<Javadoc> {
        source(android.sourceSets["main"].java.srcDirs)
        classpath += project.files(android.bootClasspath.joinToString(File.pathSeparator))
        // Exclude generated files if necessary
        exclude("**/R.class", "**/BuildConfig.class")
    }

    publishing {
        singleVariant("release") {
            // This will automatically create a publication component for the "release" variant
            withSourcesJar() // This is good, it tells AGP to include sources
            // withJavadocJar() // Uncomment if you have a Javadoc JAR task and want to include it
        }

    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.bundles.androidx.ui)

    implementation(libs.androidx.foundation.compose)
    implementation(libs.androidx.animation.compose)

    // Local libraries
    implementation(libs.kotlin.extensions.library)
}
