plugins {
    `kotlin-dsl`
}

group = "com.linkit.company.buildlogic"

gradlePlugin {
    plugins {
        register("kmpSharedConvention") {
            id = "kmp.shared.convention"
            implementationClass = "com.linkit.company.buildlogic.KmpSharedConventionPlugin"
        }
        register("kmpLibraryConvention") {
            id = "kmp.library.convention"
            implementationClass = "com.linkit.company.buildlogic.KmpLibraryConventionPlugin"
        }
        register("kmpFeatureConvention") {
            id = "kmp.feature.convention"
            implementationClass = "com.linkit.company.buildlogic.KmpFeatureConventionPlugin"
        }
        register("kmpCoreConvention") {
            id = "kmp.core.convention"
            implementationClass = "com.linkit.company.buildlogic.KmpCoreConventionPlugin"
        }
        register("androidApplicationConvention") {
            id = "android.application.convention"
            implementationClass = "com.linkit.company.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("kmpScreenshotTestConvention") {
            id = "kmp.screenshot.test.convention"
            implementationClass = "com.linkit.company.buildlogic.KmpScreenshotTestConventionPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly("dev.zacsweers.metro:dev.zacsweers.metro.gradle.plugin:${libs.versions.metro.get()}")
    compileOnly("io.github.takahirom.roborazzi:roborazzi-gradle-plugin:${libs.versions.roborazzi.get()}")
}
