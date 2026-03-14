package com.linkit.company.buildlogic

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import com.linkit.company.buildlogic.util.libs
import com.linkit.company.buildlogic.util.library

class KmpSharedConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("dev.zacsweers.metro")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            val compose = extensions.getByType<ComposeExtension>()

            extensions.configure<KotlinMultiplatformExtension> {
                compilerOptions {
                    freeCompilerArgs.add("-Xcontext-parameters")
                }

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
                        implementation(
                            libs.findLibrary("androidx.lifecycle.viewmodelCompose").get()
                        )
                        implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                    }
                    commonTest.dependencies {
                        implementation(libs.findLibrary("kotlin.test").get())
                    }

                    val iosMain by creating {
                        dependsOn(commonMain.get())
                        dependencies {
                            implementation(target.libs.library("ktorClientDarwin"))
                        }
                    }

                    listOf(
                        iosArm64Main.get(),
                        iosX64Main.get(),
                        iosSimulatorArm64Main.get(),
                    ).forEach { kotlinSourceSet ->
                        kotlinSourceSet.dependsOn(iosMain)
                    }
                }
            }

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("android.compileSdk").get().toString().toInt()

                defaultConfig {
                    minSdk = libs.findVersion("android.minSdk").get().toString().toInt()
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
