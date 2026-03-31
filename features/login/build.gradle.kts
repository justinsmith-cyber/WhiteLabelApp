plugins {
    id("whitelabel.kmp.compose")
    alias(libs.plugins.kotlinx.serialization)
}

android { namespace = "com.velsol.feature.login" }

compose.resources {
    packageOfResClass = "com.velsol.feature.login.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:theme"))
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
