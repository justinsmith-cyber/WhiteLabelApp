rootProject.name = "WhiteLabelApp"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("android.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("android.*")
            }
        }
        mavenCentral()
    }
}
include(":core:database")
include(":core:domain")
include(":core:network")
include(":features:home")
include(":features:certifications")
include(":features:inventory")
include(":sharedUI")
include(":demo-brands")
include(":brand-parity-tests")
include(":androidApp")
include(":desktopApp")

val client = settings.providers.gradleProperty("client").getOrElse("default")
include(":clients:default")
include(":clients:acme")
include(":clients:beta")
include(":clients:gamma")
