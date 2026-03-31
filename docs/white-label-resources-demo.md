# White-label resources demo (presenter notes)

## Narrative bridge: “merge” vs this repo

Android-style white-labeling often uses **priority merge**: `src/plumbingPro/res` wins over `src/main/res` for the same resource name.

This project reaches the same outcome differently: **compile-time selection** of the *active* client on the app classpath. `settings.gradle.kts` includes all `clients:*` projects and **`sharedUI` depends on exactly one** brand module: `implementation(project(":clients:$client"))`, where `client` comes from the `-Pclient` Gradle property (default `default`). The “winning” Metro binding and client resources are whichever module is on **that** edge—not two trees merged in one variant.

**One-liner for the room:** “One winning variant per build; we pick it by **which brand module ships**, not by overlaying resource folders.”

## What to show on disk

- `sharedUI/src/commonMain/composeResources/` — shared tab icons.
- `features/home/src/commonMain/composeResources/` — feature strings and drawables (`Res.string.*`).
- `clients/<name>/.../*BrandConfig.kt` — per-client colors, URLs, feature toggles, and **terminology** (`taskLabel`); only one is linked into `sharedUI` per build (`-Pclient`).

## Theming

`BrandConfig` supplies ARGB colors; `AppTheme` in `sharedUI/.../theme/Theme.kt` builds `MaterialTheme`. No raw hex in composables.

## Terminology

`BrandConfig.taskLabel` illustrates per-brand wording (e.g. generic “Task” vs plumbing- or courier-style labels). Shown on the Home hero card.

## Demo commands

Android (install debug):

```bash
./gradlew :androidApp:installDebug -Pclient=default
./gradlew :androidApp:installDebug -Pclient=acme
```

Desktop (compile check or run):

```bash
./gradlew :desktopApp:compileKotlin -Pclient=beta
./gradlew :desktopApp:run -Pclient=beta
```

Run **one** `-Pclient` value per Gradle invocation when using configuration cache, so the `sharedUI → clients:*` dependency edge stays consistent (avoid chaining two different `assembleDebug -Pclient=…` in a single command).

**Unified brand tests (optional talking point):** `core:domain` defines `BrandConfig.sameBrandContentAs` for structural equality.

iOS: the Xcode **Run Script** build phase runs `CLIENT="${CLIENT:-default}"` then `./gradlew :sharedUI:embedAndSignAppleFrameworkForXcode -Pclient="$CLIENT"`. Set **`CLIENT`** in the scheme’s environment variables (e.g. `CLIENT=acme`) to match your Android white-label build. The `embedAndSignAppleFrameworkForXcode` task expects Xcode-provided settings (target architectures); verify it by building the app from Xcode, not only from a plain CLI Gradle run.

## Suggested order (~5–7 minutes)

1. Folder layout (table above).
2. `BrandConfig` + `AppTheme`.
3. Two Android installs or two Gradle runs with different `-Pclient`.
4. Compile-time client switching via `-Pclient`.
5. `taskLabel` on Home.
6. Xcode Run Script + `CLIENT` env var.
