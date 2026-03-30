import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("dev.detekt")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

detekt {
    toolVersion = libs.findVersion("detekt").get().requiredVersion
    config.setFrom(rootProject.file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true

    val kotlinSourceRoots =
        listOf(
            "src/main/kotlin",
            "src/main/java",
            "src/commonMain/kotlin",
            "src/androidMain/kotlin",
            "src/jvmMain/kotlin",
            "src/iosMain/kotlin",
            "src/iosArm64Main/kotlin",
            "src/iosSimulatorArm64Main/kotlin",
            "src/test/kotlin",
            "src/androidTest/kotlin",
        )
            .map { layout.projectDirectory.dir(it).asFile }
            .filter { it.isDirectory }
    if (kotlinSourceRoots.isNotEmpty()) {
        source.setFrom(project.files(kotlinSourceRoots))
    }
}

dependencies {
    add("detektPlugins", libs.findLibrary("compose-rules-detekt").get())
}
