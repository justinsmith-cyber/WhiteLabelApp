plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.build.plugin.kotlin)
    compileOnly(libs.build.plugin.android)
    compileOnly(libs.build.plugin.compose)
    compileOnly(libs.build.plugin.compose.compiler)
    compileOnly(libs.build.plugin.serialization)
    compileOnly(libs.build.plugin.room)
    compileOnly(libs.build.plugin.ksp)
}
