import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.compose.multiplatform).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.kmp.library).apply(false)
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.kotlinx.serialization).apply(false)
    alias(libs.plugins.metro).apply(false)
    alias(libs.plugins.room).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.buildKonfig).apply(false)
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt).apply(false)
}

val ktlintVersion = libs.versions.ktlint.get()
val composeRulesVersion = libs.versions.composeRules.get()
val composeRulesKtlint = "io.nlopez.compose.rules:ktlint:$composeRulesVersion"

configure<SpotlessExtension> {
    kotlin {
        target("src/**/*.kt")
        targetExclude(
            "**/build/**",
            "**/bin/**",
            "**/.gradle/**",
            ".gradle/**",
        )
        ktlint(ktlintVersion)
            .setEditorConfigPath(rootProject.file(".editorconfig"))
            .customRuleSets(listOf(composeRulesKtlint))
    }
    kotlinGradle {
        target("*.gradle.kts", "build-logic/**/*.gradle.kts")
        targetExclude(
            "**/build/**",
            "**/bin/**",
            "**/.gradle/**",
            ".gradle/**",
        )
        ktlint(ktlintVersion)
            .setEditorConfigPath(rootProject.file(".editorconfig"))
            .customRuleSets(listOf(composeRulesKtlint))
    }
}
