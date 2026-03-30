plugins {
    id("whitelabel.kmp.compose")
}

android { namespace = "com.velsol.core.theme" }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
        }
    }
}
