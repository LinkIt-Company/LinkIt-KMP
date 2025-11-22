plugins {
    id("kmp.library.convention")
}

android {
    namespace = "com.linkit.company.data"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.domain)
        }
    }
}
