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

