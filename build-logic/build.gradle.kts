plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.build.plugin.kotlin)
    implementation(libs.build.plugin.android)
    implementation(libs.build.plugin.compose)
    implementation(libs.build.plugin.compose.compiler)
    implementation(libs.build.plugin.serialization)
    implementation(libs.build.plugin.room)
    implementation(libs.build.plugin.ksp)
    implementation(libs.build.plugin.detekt)
}

tasks.named("compilePluginsBlocks") {
    doFirst {
        println("SOURCES: " + (this as org.jetbrains.kotlin.gradle.tasks.KotlinCompile).sources.files.joinToString("\n"))
    }
}
