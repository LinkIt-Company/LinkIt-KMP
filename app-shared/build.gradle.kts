plugins {
    id("kmp.shared.convention")
}

android {
    namespace = "com.linkit.company"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Core modules
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)

            // Domain & Data
            implementation(projects.domain)
            implementation(projects.data)

            // Feature modules
            implementation(projects.feature.intro)
            implementation(projects.feature.home)
            implementation(projects.feature.map)
            implementation(projects.feature.schedule)
            implementation(projects.feature.storage)
            implementation(projects.feature.explore)

            // Navigation3
            implementation(libs.bundles.jetbrainsNavigation3)
        }
    }
}

