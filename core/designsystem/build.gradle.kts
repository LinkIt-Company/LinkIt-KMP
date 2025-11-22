plugins {
    id("kmp.core.convention")
}

android {
    namespace = "com.linkit.company.core.designsystem"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
        }
    }
}
