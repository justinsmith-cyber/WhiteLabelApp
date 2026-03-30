plugins {
    id("whitelabel.kmp.library")
    alias(libs.plugins.metro)
}

android { namespace = "com.velsol.clients.acmeclient" }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
        }
    }
}
