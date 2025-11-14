plugins {
    `kotlin-dsl`
}

group = "com.linkit.company.buildlogic"

gradlePlugin {
    plugins {
        register("kmpApplicationConvention") {
            id = "kmp.application.convention"
            implementationClass = "com.linkit.company.buildlogic.KmpApplicationConventionPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}
