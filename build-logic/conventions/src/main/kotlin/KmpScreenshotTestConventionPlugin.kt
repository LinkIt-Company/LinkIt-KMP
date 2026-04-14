package com.linkit.company.buildlogic

import com.android.build.gradle.LibraryExtension
import com.linkit.company.buildlogic.util.libs
import com.linkit.company.buildlogic.util.library
import io.github.takahirom.roborazzi.RoborazziExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpScreenshotTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.github.takahirom.roborazzi")
            }

            extensions.configure<RoborazziExtension> {
                outputDir.set(rootProject.file("screenshots/${project.path.replace(":", "/")}"))
                compare {
                    outputDir.set(rootProject.file("screenshots/${project.path.replace(":", "/")}"))
                }
            }

            extensions.configure<LibraryExtension> {
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    getByName("androidUnitTest") {
                        dependencies {
                            implementation(libs.library("roborazzi-core"))
                            implementation(libs.library("roborazzi-compose"))
                            implementation(libs.library("roborazzi-rule"))
                            implementation(libs.library("robolectric"))
                            implementation(libs.library("junit"))
                            implementation(libs.library("compose-ui-test-junit4"))
                            implementation(libs.library("compose-ui-test-manifest"))
                        }
                    }
                }
            }
        }
    }
}
