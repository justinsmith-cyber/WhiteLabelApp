plugins {
    id("whitelabel.kmp.compose")
    alias(libs.plugins.kotlinx.serialization)
}

android { namespace = "com.velsol.feature.inventory" }

compose.resources {
    packageOfResClass = "com.velsol.feature.inventory.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:theme"))
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
