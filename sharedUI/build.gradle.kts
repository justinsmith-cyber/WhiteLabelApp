import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("whitelabel.kmp.compose")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.metro)
    alias(libs.plugins.buildKonfig)
}

kotlin {
    targets
        .withType<KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries.withType<Framework>().configureEach { baseName = "SharedUI" }
        }

    sourceSets {
        val client = providers.gradleProperty("client").getOrElse("default")

        commonMain.dependencies {
            api(project(":core:domain"))
            api(project(":core:theme"))
            implementation(project(":clients:$client"))
            implementation(project(":core:database"))
            implementation(project(":core:network"))
            implementation(project(":features:home"))
            implementation(project(":features:certifications"))
            implementation(project(":features:inventory"))
            implementation(project(":features:login"))
            implementation(libs.kermit)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.compose.ui.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android { namespace = "com.velsol" }

compose.resources {
    packageOfResClass = "com.velsol.generated.resources"
}

buildkonfig {
    packageName = "com.velsol"
    val clientId = providers.gradleProperty("client").getOrElse("default")
    defaultConfigs {
        buildConfigField(STRING, "CLIENT_ID", clientId)
    }
}
