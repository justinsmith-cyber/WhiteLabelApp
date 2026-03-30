# Agent guide — WhiteLabelApp

Kotlin Multiplatform white-label demo: one **active client** per Gradle configuration via `-Pclient` (default `default`). Full module tree, `BrandConfig` contract, and runbooks live in [README.MD](README.MD).

---

## Quick reference

```bash
# Android debug (default client)
./gradlew :androidApp:assembleDebug

# Same with a named client
./gradlew :androidApp:assembleRelease -Pclient=acme
./gradlew :desktopApp:run -Pclient=beta

# JVM tests (contract + parity)
./gradlew :core:domain:jvmTest :sharedUI:jvmTest :brand-parity-tests:test
```

Clients in this repo: `default`, `acme`, `beta`, `gamma`. CI matrix: [.github/workflows/build-clients.yml](.github/workflows/build-clients.yml).

---

## Version control (Jujutsu)

Use **[Jujutsu](https://jj-vcs.dev/)** (`jj`) for local history in this repo: `jj status`, `jj diff`, `jj log`, `jj commit`, etc. For Git remotes, prefer `jj git fetch` and `jj git push` over raw `git pull` / `git push` when a `.jj/` directory is present.

Full workflow (clone, colocation, bookmarks, export/import): [docs/jj-version-control.md](docs/jj-version-control.md).

CI remains defined under [`.github/workflows/`](.github/workflows/) and expects a Git remote as today.

---

## Architecture at a glance

- **`core:domain`** — Pure contracts: `BrandConfig`, `FeatureToggles`, `AppScope`; no DI/Compose/Android.
- **`core:database`** / **`core:network`** — Room KMP and Ktor client wiring.
- **`features/*`** — Product areas; each exposes a Decompose component surface and composable content.
- **`sharedUI`** — Root Decompose router, `AppGraph` (Metro), theming, `App` entry composable.
- **`clients/*`** — One brand module per client; **exactly one** is linked into `sharedUI` for a given build (see `settings.gradle.kts` + `sharedUI` dependency on `:clients:{client}`).
- **`demo-brands`** — Inline demo `BrandConfig` rows for the in-app switcher and tests; does **not** depend on `clients:*`.
- **`brand-parity-tests`** — JVM tests: live client configs vs demo rows.
- **Shells** — `androidApp`, `desktopApp`, `iosApp` (Xcode).

For navigation stacks, network behavior, and file-level patterns, use [README.MD](README.MD) (Decompose, unified testing, network sections).

---

## Non-negotiables

1. **Domain purity** — No Metro, Compose, or Android APIs in `core:domain`. Consume brand data in UI via `LocalBrandConfig` (not threaded parameters); see the brand rule below.
2. **Single graph** — One Metro `@DependencyGraph` (`AppGraph` in `sharedUI`). No second graph.
3. **Decompose shape** — Per feature: interface + `Default*` implementation + `*Content` composable. Instantiate feature components from `DefaultRootComponent` only; follow the rule when adding screens or tabs.
4. **Gradle** — Use `build-logic` convention plugins (`whitelabel.kmp.*`). Do not duplicate KMP/Compose/Android SDK setup in module `build.gradle.kts` files.

---

## Project Cursor rules (read when relevant)

These rules are **supplemental** to this file. When your task touches the topic, **follow the matching rule** (they use `globs` / `alwaysApply: false`).

| Rule | Focus |
|------|--------|
| [.cursor/rules/brand-config.mdc](.cursor/rules/brand-config.mdc) | `BrandConfig`, `FeatureToggles`, `@ContributesBinding`, `AppGraph`, `LocalBrandConfig` |
| [.cursor/rules/decompose-navigation.mdc](.cursor/rules/decompose-navigation.mdc) | Feature component triple, `RootComponent` / `DefaultRootComponent` extension steps |
| [.cursor/rules/gradle-convention-plugins.mdc](.cursor/rules/gradle-convention-plugins.mdc) | `whitelabel.kmp.library` / `compose` / `database` / `network` / `android.library` templates |
| [.cursor/rules/jujutsu-vcs.mdc](.cursor/rules/jujutsu-vcs.mdc) | Prefer `jj` for local VCS; `jj git` for remote; CI still GitHub/Git |

---

## Further documentation

- [docs/jj-version-control.md](docs/jj-version-control.md) — Jujutsu (`jj`) usage: install, clone, daily commands, Git remote sync.
- [docs/white-label-resources-demo.md](docs/white-label-resources-demo.md) — Presenter notes: compile-time client selection vs resource merge, demo switcher limits, theming.
- [README.MD](README.MD) — Long-form structure diagram, client table, `BrandConfig` snippet, iOS notes, and feature-specific detail.

---

## Extending agent guidance

- **Repository-specific** — Add or edit [.cursor/rules/](.cursor/rules/) (`.mdc` with YAML frontmatter: `description`, `globs`, `alwaysApply` as needed). Keep narrow scope per rule.
- **Editor and personal workflows** — Use Cursor **Skills** from your skills library when authoring new rules or skills, or when changing editor settings (e.g. “Create Cursor rules”, “Create Skill”, “Modify Cursor settings” skills if installed).

For product behavior of AGENTS.md and `.cursor/rules` in Cursor itself, see [Cursor documentation](https://cursor.com/docs).
