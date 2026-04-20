pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ritm-android"

include(":app")
include(":core-common")
include(":core-ui")
include(":core-database")
include(":feature-home")
include(":feature-cycle")
include(":feature-water")
include(":feature-fasting")
include(":feature-habits")
include(":feature-settings")
