plugins {
    id("whitelabel.kmp.library")
    alias(libs.plugins.metro)
}

android { namespace = "com.velsol.clients.gammaclient" }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
        }
    }
}
