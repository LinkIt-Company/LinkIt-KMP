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

            // Domain & Data
            implementation(projects.domain)
            implementation(projects.data)
        }
    }
}

