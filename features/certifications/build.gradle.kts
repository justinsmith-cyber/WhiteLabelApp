plugins {
    id("whitelabel.kmp.compose")
    alias(libs.plugins.kotlinx.serialization)
}

android { namespace = "com.velsol.feature.certifications" }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:network"))
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
