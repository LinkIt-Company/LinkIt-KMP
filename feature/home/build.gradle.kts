plugins {
    id("kmp.feature.convention")
}

android {
    namespace = "com.linkit.company.feature.home"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(libs.metrox.viewmodel)
            implementation(libs.metrox.viewmodel.compose)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(libs.bundles.jetbrainsNavigation3)
            implementation(projects.domain)

            // 탭 모듈
            implementation(projects.feature.map)
            implementation(projects.feature.storage)
            implementation(projects.feature.explore)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.metrox.android)
        }
    }
}
