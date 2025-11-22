plugins {
    id("kmp.application.convention")
}

android {
    namespace = "com.linkit.company"

    defaultConfig {
        applicationId = "com.linkit.company"
        versionCode = libs.versions.app.versionCode.get().toInt()
        versionName = libs.versions.app.versionName.get()
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Core modules
            implementation(projects.core.common)
            implementation(projects.core.designsystem)
            
            // Domain & Data
            implementation(projects.domain)
            implementation(projects.data)
            
            // Feature modules
            implementation(projects.feature.home)
            implementation(projects.feature.classification)
            implementation(projects.feature.onboarding)
            implementation(projects.feature.save)
            implementation(projects.feature.share)
            implementation(projects.feature.storage)
        }
    }
}

