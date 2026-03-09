plugins {
    id("kmp.feature.convention")
}

android {
    namespace = "com.linkit.company.feature.explore"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.designsystem)
            implementation(projects.domain)
        }
    }
}
