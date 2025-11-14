package com.linkit.company.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.application")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            
            val compose = extensions.getByType<ComposeExtension>()

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                }

                listOf(
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                    }
                }

                sourceSets.apply {
                    androidMain.dependencies {
                        implementation(libs.findLibrary("androidx.activity.compose").get())
                    }
                    commonMain.dependencies {
                        implementation(compose.dependencies.runtime)
                        implementation(compose.dependencies.foundation)
                        implementation(compose.dependencies.material3)
                        implementation(compose.dependencies.ui)
                        implementation(compose.dependencies.components.resources)
                        implementation(compose.dependencies.components.uiToolingPreview)
                        implementation(libs.findLibrary("androidx.lifecycle.viewmodelCompose").get())
                        implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                    }
                    commonTest.dependencies {
                        implementation(libs.findLibrary("kotlin.test").get())
                    }
                }
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = libs.findVersion("android.compileSdk").get().toString().toInt()

                defaultConfig {
                    minSdk = libs.findVersion("android.minSdk").get().toString().toInt()
                    targetSdk = libs.findVersion("android.targetSdk").get().toString().toInt()
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }

            dependencies {
                "debugImplementation"(compose.dependencies.uiTooling)
            }
        }
    }
}
