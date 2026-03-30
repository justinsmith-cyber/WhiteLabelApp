import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("whitelabel.kmp.library")
    id("androidx.room")
    id("com.google.devtools.ksp")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.findLibrary("room-runtime").get())
        }
    }
}

room { schemaDirectory("$projectDir/schemas") }

dependencies {
    with(libs.findLibrary("room-compiler").get()) {
        add("kspAndroid", this)
        add("kspJvm", this)
        add("kspIosArm64", this)
        add("kspIosSimulatorArm64", this)
    }
}
