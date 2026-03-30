import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.jvm)
    id("whitelabel.detekt")
}

dependencies {
    implementation(project(":sharedUI"))
    implementation(libs.decompose)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "WhiteLabelApp"
            packageVersion = "1.0.0"

            linux {
                val f = project.file("appIcons/LinuxIcon.png")
                if (f.exists()) iconFile.set(f)
            }
            windows {
                val f = project.file("appIcons/WindowsIcon.ico")
                if (f.exists()) iconFile.set(f)
            }
            macOS {
                val f = project.file("appIcons/MacosIcon.icns")
                if (f.exists()) iconFile.set(f)
                bundleID = "com.velsol.desktopApp"
            }
        }
    }
}
