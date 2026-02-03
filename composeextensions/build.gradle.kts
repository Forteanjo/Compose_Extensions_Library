import org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.dokka)

    id("maven-publish")

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
        }
    }

    buildFeatures {
        compose = true

        // Disable unused AGP features
        viewBinding = false
        dataBinding = false
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

// Creates a JAR file from the output of the dokkaHtml task.
// This is used to publish the documentation along with the library.
tasks.register<Jar>("javadocJar") {
    dependsOn(tasks.named("dokkaGenerateHtml"))
    archiveClassifier.set("javadoc")
    from(tasks.named("dokkaGenerateHtml").map { it.outputs.files })
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.forteanjo"
            artifactId = "compose-extensions"
            version = "1.0.1"

            afterEvaluate {
                from(components["release"])
            }

            artifact(tasks.named("javadocJar"))

            pom {
                name.set(project.name)
                description.set("Useful Jetpack compose extensions and modifiers for Android development.")
                url.set("https://github.com/Forteanjo/Compose_Extensions_Library")

                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("http://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("forteanjo")
                        name.set("Donald McCaskey")
                        email.set("forteanjo@sky.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/forteanjo/Compose_Extensions_Library.git")
                    developerConnection.set("scm:git:ssh://github.com/forteanjo/Compose_Extensions_Library.git")
                    url.set("https://github.com/Forteanjo/Compose_Extensions_Library/tree/main")
                }
            }
        }
    }

    repositories {
        maven {
            name = "localRelease"
            url = uri("${layout.buildDirectory}/repo")
        }
    }
}
