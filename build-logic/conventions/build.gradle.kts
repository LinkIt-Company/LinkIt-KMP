plugins {
    `kotlin-dsl`
}

group = "com.linkit.company.buildlogic"

gradlePlugin {
    plugins {
        register("kmpApplicationConvention") {
            id = "kmp.application.convention"
            implementationClass = "com.linkit.company.buildlogic.KmpApplicationConventionPlugin"
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
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}
