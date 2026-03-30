plugins {
    id("whitelabel.kmp.compose")
    alias(libs.plugins.kotlinx.serialization)
}

android { namespace = "com.velsol.feature.inventory" }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
        }
    }
}
