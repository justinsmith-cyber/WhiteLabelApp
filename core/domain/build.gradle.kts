plugins { id("whitelabel.kmp.library") }

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android { namespace = "com.velsol.core.domain" }
