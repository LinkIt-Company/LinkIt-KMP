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
            implementation(libs.metrox.viewmodel)

            // Domain & Data
            implementation(projects.domain)
            implementation(projects.data)
        }
    }
}

