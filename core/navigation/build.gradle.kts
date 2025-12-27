plugins {
    id("kmp.core.convention")
}

android {
    namespace = "com.linkit.company.core.navigation"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(libs.bundles.jetbrainsNavigation3)
            implementation(libs.kotlinx.serialization.core)
        }
    }
}
