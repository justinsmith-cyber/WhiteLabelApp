plugins {
    id("whitelabel.kmp.library")
    alias(libs.plugins.metro)
}

android { namespace = "com.velsol.clients.betaclient" }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
        }
    }
}
