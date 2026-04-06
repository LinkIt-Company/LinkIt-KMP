plugins {
    id("kmp.feature.convention")
}

android {
    namespace = "com.linkit.company.feature.intro"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.domain)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.metrox.android)
            implementation(libs.metrox.viewmodel)
        }
    }
}
