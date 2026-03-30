plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(project(":core:domain"))
    testImplementation(project(":demo-brands"))
    testImplementation(project(":clients:default"))
    testImplementation(project(":clients:acme"))
    testImplementation(project(":clients:beta"))
    testImplementation(project(":clients:gamma"))
}
