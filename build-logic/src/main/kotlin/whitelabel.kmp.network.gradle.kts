import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("whitelabel.kmp.library")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.findLibrary("ktor-client-core").get())
            implementation(libs.findLibrary("ktor-client-content-negotiation").get())
            implementation(libs.findLibrary("ktor-serialization-json").get())
            implementation(libs.findLibrary("ktor-client-logging").get())
            implementation(libs.findLibrary("kotlinx-serialization-json").get())
        }
        androidMain.dependencies {
            implementation(libs.findLibrary("ktor-client-okhttp").get())
        }
        jvmMain.dependencies {
            implementation(libs.findLibrary("ktor-client-okhttp").get())
        }
        iosMain.dependencies {
            implementation(libs.findLibrary("ktor-client-darwin").get())
        }
    }
}
