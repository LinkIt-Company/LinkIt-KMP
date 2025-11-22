rootProject.name = "LinkitCompany"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":app")

// Core modules
include(":core:common")
include(":core:designsystem")

// Domain
include(":domain")

// Data
include(":data")

// Features
include(":feature:home")
include(":feature:classification")
include(":feature:onboarding")
include(":feature:save")
include(":feature:share")
include(":feature:storage")
