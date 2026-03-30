---
name: gradle-lint
description: >-
  When the user or task involves Spotless, ktlint, Detekt, code formatting checks, or the lint CI job
  in the WhiteLabelApp repository — use this skill and the linting rule.
---

# Gradle lint — Spotless & Detekt (WhiteLabelApp)

## Defaults

1. **Formatting** is enforced by **Spotless** + **ktlint** (with **compose-rules** for Kotlin/Compose). Config: root `.editorconfig`, versions in `gradle/libs.versions.toml`.
2. **Static analysis** is **Detekt 2.x** (`dev.detekt` plugin id) with **`config/detekt/detekt.yml`** and **compose-rules** as `detektPlugins`, wired through **`whitelabel.detekt`** in build-logic.
3. Do not enable **`detekt-formatting`**; it duplicates ktlint relative to Spotless.

## Commands

| Task | Command |
|------|---------|
| Check formatting | `./gradlew spotlessCheck` |
| Apply formatting | `./gradlew spotlessApply` |
| Run Detekt (all modules) | `./gradlew detekt --continue` |
| Match CI | `./gradlew spotlessCheck detekt --continue` |

## Editing the setup

- Bump tool versions in **`gradle/libs.versions.toml`** (keep **ktlint** aligned with the [compose-rules ktlint matrix](https://mrmans0n.github.io/compose-rules/ktlint/)).
- Adjust Detekt rule overrides in **`config/detekt/detekt.yml`**.
- New Kotlin module using **`whitelabel.kmp.library`** / **`whitelabel.android.library`** gets Detekt automatically. Shell-only modules need **`id("whitelabel.detekt")` applied last** in their `plugins` block.

## Full detail

See [.cursor/rules/linting-spotless-detekt.mdc](../../rules/linting-spotless-detekt.mdc), [docs/code-quality.md](../../../docs/code-quality.md), and [AGENTS.md](../../../AGENTS.md).
