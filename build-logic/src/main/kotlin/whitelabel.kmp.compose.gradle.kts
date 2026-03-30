import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("whitelabel.kmp.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.findLibrary("compose-runtime").get())
            api(libs.findLibrary("compose-ui").get())
            api(libs.findLibrary("compose-foundation").get())
            api(libs.findLibrary("compose-resources").get())
            api(libs.findLibrary("compose-ui-tooling-preview").get())
            api(libs.findLibrary("compose-material3").get())
            api(libs.findLibrary("decompose").get())
            api(libs.findLibrary("decompose-compose").get())
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

dependencies {
    "debugImplementation"(libs.findLibrary("compose-ui-tooling").get())
}
