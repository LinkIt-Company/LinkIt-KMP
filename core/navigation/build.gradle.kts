plugins {
    id("kmp.core.convention")
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.linkit.company.core.navigation"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.feature.home)
            implementation(projects.feature.classification)
            implementation(projects.feature.onboarding)
            implementation(projects.feature.save)
            implementation(projects.feature.share)
            implementation(projects.feature.storage)
            implementation(libs.bundles.jetbrainsNavigation3)
            implementation(libs.kotlinx.serialization.core)
        }
    }
}
