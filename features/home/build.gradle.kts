plugins {
    id("whitelabel.kmp.compose")
}

android { namespace = "com.velsol.feature.home" }

compose.resources {
    packageOfResClass = "com.velsol.feature.home.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:theme"))
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
