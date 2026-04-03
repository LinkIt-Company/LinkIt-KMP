plugins {
    id("kmp.core.convention")
}

android {
    namespace = "com.linkit.company.core.common"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.activity.compose)
            implementation(libs.metrox.viewmodel)
        }
    }
}
