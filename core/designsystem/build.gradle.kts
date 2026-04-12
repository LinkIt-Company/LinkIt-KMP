plugins {
    id("kmp.core.convention")
}

android {
    namespace = "com.linkit.company.core.designsystem"
}

val composeExtension = extensions.getByType(org.jetbrains.compose.ComposeExtension::class.java)

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(composeExtension.dependencies.materialIconsExtended)
        }
    }
}
