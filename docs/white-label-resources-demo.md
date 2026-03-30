# White-label resources demo (presenter notes)

## Narrative bridge: “merge” vs this repo

Android-style white-labeling often uses **priority merge**: `src/plumbingPro/res` wins over `src/main/res` for the same resource name.

This project reaches the same outcome differently: **compile-time selection** of the *active* client on the app classpath. `settings.gradle.kts` includes **every** `clients:*` project (default, acme, beta, gamma) so tools such as `:brand-parity-tests` can depend on them all. **`sharedUI` still depends on exactly one** brand module: `implementation(project(":clients:$client"))`, where `client` comes from the `-Pclient` Gradle property (default `default`). The “winning” Metro binding and client resources are whichever module is on **that** edge—not two trees merged in one variant.

**One-liner for the room:** “One winning variant per build; we pick it by **which brand module ships**, not by overlaying resource folders.”

## What to show on disk

- `sharedUI/src/commonMain/composeResources/` — shared tab icons.
- `features/home/src/commonMain/composeResources/` — feature strings and drawables (`Res.string.*`).
- `clients/<name>/.../*BrandConfig.kt` — per-client colors, URLs, feature toggles, and **terminology** (`taskLabel`); only one is linked into `sharedUI` per build (`-Pclient`).
- `demo-brands/.../DemoBrandConfigs.kt` — the four inline demo `BrandConfig` rows for the runtime switcher (depends only on `core:domain`, not on `clients:*`).

## Theming

`BrandConfig` supplies ARGB colors; `AppTheme` in `sharedUI/.../theme/Theme.kt` builds `MaterialTheme`. No raw hex in composables.

## Demo switcher caveat

`DemoClientSwitcher` swaps **in-memory** `BrandConfig` instances for UI preview. It does **not** change which `clients:*` module was compiled in or reload Compose Resources from another build. Say clearly: “Preview in-app; full brand + DI binding = rebuild with `-Pclient=`.”

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

**Unified brand tests (optional talking point):** `core:domain` defines `BrandConfig.sameBrandContentAs` for structural equality; `sharedUI` common tests assert the demo list; `:brand-parity-tests` compares live `*BrandConfig` classes to `allDemoConfigs` on the JVM.

iOS: the Xcode **Run Script** build phase runs `CLIENT="${CLIENT:-default}"` then `./gradlew :sharedUI:embedAndSignAppleFrameworkForXcode -Pclient="$CLIENT"`. Set **`CLIENT`** in the scheme’s environment variables (e.g. `CLIENT=acme`) to match your Android white-label build. The `embedAndSignAppleFrameworkForXcode` task expects Xcode-provided settings (target architectures); verify it by building the app from Xcode, not only from a plain CLI Gradle run.

## Suggested order (~5–7 minutes)

1. Folder layout (table above).
2. `BrandConfig` + `AppTheme`.
3. Two Android installs or two Gradle runs with different `-Pclient`.
4. Demo switcher + preview vs rebuild.
5. `taskLabel` on Home.
6. Xcode Run Script + `CLIENT` env var.
