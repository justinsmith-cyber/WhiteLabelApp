# Code quality — Spotless and Detekt

This project enforces formatting with **Spotless** + **ktlint** (including [compose-rules](https://mrmans0n.github.io/compose-rules/ktlint/)) and static analysis with **Detekt 2.x** and the [compose-rules Detekt artifact](https://mrmans0n.github.io/compose-rules/detekt/).

## Commands

| Command | Purpose |
|---------|---------|
| `./gradlew spotlessCheck` | Fail if Kotlin or `*.kts` scripts violate ktlint / compose-rules |
| `./gradlew spotlessApply` | Apply ktlint fixes where possible |
| `./gradlew detekt --continue` | Run Detekt on all subprojects; keep going after failures |
| `./gradlew spotlessCheck detekt --continue` | Same as the **`lint`** job in [`.github/workflows/build-clients.yml`](../.github/workflows/build-clients.yml) |

## Configuration

| Piece | Location |
|-------|----------|
| Spotless + plugin catalog resolution | Root [`build.gradle.kts`](../build.gradle.kts) |
| Tool versions | [`gradle/libs.versions.toml`](../gradle/libs.versions.toml) |
| ktlint / EditorConfig | [`.editorconfig`](../.editorconfig) (repo root) |
| Detekt YAML | [`config/detekt/detekt.yml`](../config/detekt/detekt.yml) |
| Per-module Detekt wiring | [`whitelabel.detekt`](../build-logic/src/main/kotlin/whitelabel.detekt.gradle.kts) (applied from `whitelabel.kmp.library`, `whitelabel.android.library`, or explicitly on shell/JVM-only modules) |

Detekt uses the **`dev.detekt`** Gradle plugin id (Detekt 2.x). Do not add **`detekt-formatting`**; formatting stays in Spotless only.

## Cursor / agents

- Rule: [`.cursor/rules/linting-spotless-detekt.mdc`](../.cursor/rules/linting-spotless-detekt.mdc)
- Skill: [`.cursor/skills/gradle-lint/SKILL.md`](../.cursor/skills/gradle-lint/SKILL.md)
- Summary: [`AGENTS.md`](../AGENTS.md)
