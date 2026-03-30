plugins { id("whitelabel.kmp.library") }

android { namespace = "com.velsol.demo.brands" }

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
        }
    }
}
