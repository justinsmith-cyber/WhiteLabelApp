plugins {
    id("whitelabel.kmp.compose")
    alias(libs.plugins.kotlinx.serialization)
}

android { namespace = "com.velsol.feature.messages" }

compose.resources {
    packageOfResClass = "com.velsol.feature.messages.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:theme"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
