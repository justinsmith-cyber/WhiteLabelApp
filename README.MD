# WhiteLabelApp

A Kotlin Multiplatform demo showing how a single shared codebase can produce multiple distinct branded apps — different colors, logos, app names, feature sets, and API endpoints — from one build-time switch.

## Version control

Day-to-day history for this repository is managed with **[Jujutsu](https://jj-vcs.dev/)** (`jj`); pushes and CI still use **Git** (see `.github/workflows/`). Commands, clone/init, and remote sync are documented in [docs/jj-version-control.md](docs/jj-version-control.md).

## Project Structure

```
WhiteLabelApp/
├── .github/
│   └── workflows/
│       └── build-clients.yml       # Matrix CI: builds every client flavor for Android + Desktop
├── build-logic/                    # Convention plugins (shared Gradle config)
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src/main/kotlin/
│       ├── whitelabel.kmp.library.gradle.kts   # KMP targets + Android SDK config
│       ├── whitelabel.kmp.compose.gradle.kts   # Compose MP + Decompose + all common Compose deps
│       ├── whitelabel.kmp.database.gradle.kts  # Room runtime + KSP compiler
│       ├── whitelabel.kmp.network.gradle.kts   # Ktor client + platform engines + serialization
│       └── whitelabel.android.library.gradle.kts  # Android-only library preset
├── gradle/
│   └── libs.versions.toml          # Single source of truth for all versions
├── core/
│   ├── domain/                     # Contracts: BrandConfig interface, FeatureToggles, AppScope
│   ├── database/                   # Room KMP definitions & migrations (AppDatabase)
│   └── network/                    # Ktor HttpClient factory & API interceptors
├── clients/
│   ├── default/                    # Generic white-label (no feature flags)
│   ├── acme/                       # Acme Field Services (red, HVAC certifications)
│   ├── beta/                       # Beta Plumbing Co (blue, plumbing inventory)
│   └── gamma/                      # Gamma Field Tech (green, all features enabled)
├── demo-brands/                    # KMP library: all four demo BrandConfig rows (core:domain only)
├── brand-parity-tests/             # JVM tests: live client BrandConfig vs demo list (same contract)
├── features/
│   ├── home/                       # Home screen (always shown); hero card + feature tiles
│   ├── certifications/             # HVAC certifications tracker; list + detail screens
│   └── inventory/                  # Plumbing inventory tracker; list + detail screens
├── sharedUI/                       # Root Decompose router; wires features and core together
│   └── src/
│       ├── commonMain/kotlin/com/velsol/
│       │   ├── App.kt              # Entry-point composable — accepts RootComponent + BrandConfig
│       │   ├── demo/
│       │   │   └── DemoClientSwitcher.kt # ModalBottomSheet switcher UI (configs from :demo-brands)
│       │   ├── di/
│       │   │   ├── AppGraph.kt         # Metro @DependencyGraph — collects client bindings at compile time
│       │   │   └── AppGraphFactory.kt  # Instantiates AppGraph; exposes appBrandConfig()
│       │   ├── root/
│       │   │   ├── RootComponent.kt           # Interface + sealed Child hierarchy
│       │   │   ├── DefaultRootComponent.kt    # ChildStack (Home) + tab children (Certifications, Inventory)
│       │   │   └── RootContent.kt             # Scaffold + NavigationBar + per-tab Children composable
│       │   └── theme/              # AppTheme (brand-aware Material3 colors), LocalBrandConfig, LocalThemeIsDark
│       └── iosMain/kotlin/main.kt  # MainViewController — creates DefaultRootComponent; holds mutable BrandConfig state
├── androidApp/                     # Android application shell
├── desktopApp/                     # Desktop (JVM) application shell
└── iosApp/                         # iOS application shell (Xcode)
```

## Module Architecture

The project uses a layered structure:

- **`core/`** — Foundation singletons shared by every feature.
  - `core:domain` — Pure KMP contracts: `BrandConfig` interface, `FeatureToggles` data class, `AppScope` scope marker. No DI framework dependency.
  - `core:database` — Room KMP definitions and migrations.
  - `core:network` — Ktor `HttpClient` factory (`createHttpClient()`).
- **`features/`** — One module per product domain. Each exports a Decompose component hierarchy and `@Composable` content functions.
- **`clients/`** — White-label layer; one module per client. Each implements `core:domain` interfaces and contributes its bindings to `AppScope` via Metro's `@ContributesBinding`. All client projects are included in the composite build; the **active** client on the `sharedUI` classpath is selected at configuration time via the `client` Gradle property (`-Pclient`).
- **`demo-brands`** — Small KMP library holding `allDemoConfigs` (four inline `BrandConfig` implementations). Used by `sharedUI` for the runtime switcher and by `:brand-parity-tests`; it does **not** depend on any `clients:*` module.
- **`sharedUI`** — Root Decompose router; holds `AppGraph` (Metro DI), `RootComponent`, and `RootContent`. Depends on `:demo-brands` and **one** `:clients:{client}` module. Also hosts the demo switcher UI (`demo/DemoClientSwitcher.kt`).

---

## White-Label Builds

The active client is selected via the `client` Gradle property. When omitted, `default` is used.

```bash
# Default client
./gradlew :androidApp:assembleDebug

# Build a specific client
./gradlew :androidApp:assembleRelease -Pclient=acme
./gradlew :desktopApp:run             -Pclient=acme

./gradlew :androidApp:assembleRelease -Pclient=beta
./gradlew :desktopApp:run             -Pclient=beta

./gradlew :androidApp:assembleRelease -Pclient=gamma
./gradlew :desktopApp:run             -Pclient=gamma
```

Four clients ship in this repo:

| Client | App Name | Tagline | Primary | Secondary | Tertiary | Features |
|---|---|---|---|---|---|---|
| `default` | WhiteLabel App | Your workflow, your way | Purple `#6750A4` | Teal `#00897B` | Mauve `#7D5260` | none |
| `acme` | Acme Field Services | Certified HVAC Excellence | Red `#E53935` | Orange `#F57F17` | Blue `#1565C0` | `hasHvacCertifications` |
| `beta` | Beta Plumbing Co | Precision Plumbing Solutions | Blue `#1565C0` | Cyan `#00ACC1` | Purple `#6A1B9A` | `hasPlumbingInventory` |
| `gamma` | Gamma Field Tech | Complete field operations platform | Green `#2E7D32` | Amber `#F9A825` | Teal `#00695C` | both flags |

`settings.gradle.kts` includes **all** `:clients:*` projects so aggregate checks (for example `:brand-parity-tests`) can reference every `*BrandConfig` in one JVM test run. **`sharedUI` still resolves exactly one** client: it reads `-Pclient` at configuration time and adds `implementation(project(":clients:{client}"))`. iOS follows the same flag — the `SharedUI` framework is built with that client’s Metro binding baked in.

---

## BrandConfig Contract

Every client module implements `BrandConfig` from `core:domain`:

```kotlin
interface BrandConfig {
    val appName: String
    val tagline: String
    val taskLabel: String               // e.g. "Task", "Job", "Delivery"
    val apiBaseUrl: String
    val supportEmail: String
    val primaryColorArgb: Long
    val onPrimaryColorArgb: Long
    val secondaryColorArgb: Long
    val onSecondaryColorArgb: Long
    val tertiaryColorArgb: Long       // drives Material3 tertiary slot
    val onTertiaryColorArgb: Long
    val logoUrl: String               // empty string = no logo; loaded by Coil in the hero card
    val features: FeatureToggles
}

data class FeatureToggles(
    val hasHvacCertifications: Boolean = false,
    val hasPlumbingInventory: Boolean = false,
)
```

`AppTheme` in `sharedUI` maps `primaryColorArgb`, `secondaryColorArgb`, and `tertiaryColorArgb` (plus their `on*` counterparts) directly into `MaterialTheme.colorScheme`'s corresponding slots. All other palette roles use fixed seeds.

`logoUrl` is loaded by Coil's `AsyncImage` inside `HeroBrandCard` on the home screen. Leave it as an empty string for clients without a logo.

`core:domain` also provides `BrandConfig.sameBrandContentAs(other)` — structural equality across different implementing types (every scalar field plus `FeatureToggles`). Use it in tests so demo rows, client classes, and expectations share one definition of “same brand.”

---

## Unified testing

| Module | What it checks |
|--------|------------------|
| `:core:domain` | `commonTest`: contract of `sameBrandContentAs` (reflexive, symmetric, field-sensitive) and `FeatureToggles` defaults. |
| `:sharedUI` | `commonTest`: `allDemoConfigs` size, unique names, each row matches expected inline `BrandConfig` via `sameBrandContentAs`. |
| `:brand-parity-tests` | JVM `test`: `DefaultBrandConfig`, `AcmeBrandConfig`, `BetaBrandConfig`, `GammaBrandConfig` instances match the corresponding demo row from `:demo-brands`. |

Fast local run (JVM only):

```bash
./gradlew :core:domain:jvmTest :sharedUI:jvmTest :brand-parity-tests:test
```

---

## Decompose Navigation Pattern

### Feature module structure

Each feature module exposes a component hierarchy and matching content composables:

| File | Purpose |
|---|---|
| `XxxComponent` | Interface declaring the `ChildStack` and any cross-cutting callbacks |
| `DefaultXxxComponent` | Concrete implementation; owns `StackNavigation<Config>` and creates child components |
| `XxxListComponent` | Interface for the list screen; exposes `StateFlow` data and `onItemSelected` |
| `XxxDetailComponent` | Interface for the detail screen; exposes the selected item key and `onBack()` |
| `XxxContent` | `@Composable` list screen — receives the list component |
| `XxxDetailContent` | `@Composable` detail screen — receives the detail component |

### In-tab stacks (certifications + inventory)

Both `certifications` and `inventory` have their own internal `ChildStack`, enabling full back-stack navigation within each tab:

```
CertificationsComponent.stack
  ├── Config.List  → CertificationsContent (list, fetches from network)
  └── Config.Detail(certName) → CertificationDetailContent (reads cert by name)

InventoryComponent.stack
  ├── Config.List  → InventoryContent (list)
  └── Config.Detail(sku) → InventoryDetailContent (reads item by SKU)
```

`RootContent` renders `Children(component.certifications.stack)` and `Children(component.inventory.stack)` — not the flat content directly — so each tab gets proper Decompose lifecycle management and back-button handling.

### Root stack (home)

The root `ChildStack` in `DefaultRootComponent` currently contains only `Config.Home`. Tab switching is driven by `selectedTab` local state in `RootContent`.

```
DefaultRootComponent
  ├── stack (ChildStack)
  │   └── Config.Home → DefaultHomeComponent
  ├── certifications (childContext) → DefaultCertificationsComponent
  │   └── internal stack: List | Detail
  └── inventory (childContext) → DefaultInventoryComponent
      └── internal stack: List | Detail
```

---

## Network Layer

The certifications feature is wired to the real network. `DefaultCertListComponent` creates a `CertificationsRepository` backed by `CertificationsNetworkDataSource` (Ktor `HttpClient`). On any network error or empty response the repository falls back to `mockCertifications`, so the demo works offline.

```
CertificationsNetworkDataSource  →  GET {apiBaseUrl}/certifications
        ↓ (on success, non-empty)
CertificationsRepository.getCertifications()
        ↓ (on error / 404 / empty)
        mockCertifications  (always 5 items)
```

The `HttpClient` instance is created by `createHttpClient()` from `core:network` and held on `DefaultRootComponent`, which passes `httpClient` and `apiBaseUrl` down to `DefaultCertificationsComponent`.

---

## Runtime Demo Switcher

> Triple-tap the app name on the Home screen to open the switcher.

The demo switcher lets stakeholders change the entire brand — colors, name, logo, feature tabs — without rebuilding. Brand rows live in **`demo-brands`** (only `core:domain`); the sheet UI lives in **`sharedUI/demo/`**.

- `demo-brands/.../DemoBrandConfigs.kt` — `allDemoConfigs`: four self-contained inline `object` implementations of `BrandConfig`, one per client persona.
- `sharedUI/.../demo/DemoClientSwitcher.kt` — `ModalBottomSheet` that lists all configs as tappable cards showing the brand name, tagline, and a three-dot color swatch.

All three platform entry points (`AppActivity`, `main.kt`, `MainViewController`) hold `var brandConfig by mutableStateOf(appBrandConfig())`. Selecting a config from the sheet calls `onBrandSelected(config)`, updating the state and triggering a full recomposition of `AppTheme`, including colors, logo, and which tabs are visible.

---

## Adding a New Client

1. Register the module in `settings.gradle.kts`: add `include(":clients:{name}")` next to the other client includes.

2. Create `clients/{name}/build.gradle.kts`:

```kotlin
plugins {
    id("whitelabel.kmp.library")
    alias(libs.plugins.metro)
}
android { namespace = "com.velsol.clients.{name}client" }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
        }
    }
}
```

3. Implement `BrandConfig` in `commonMain` with `@ContributesBinding(AppScope::class)` + `@Inject`:

```kotlin
@ContributesBinding(AppScope::class)
@Inject
class YourBrandConfig : BrandConfig {
    override val appName = "Your App"
    override val tagline = "Your tagline"
    override val taskLabel = "Task"
    override val apiBaseUrl = "https://api.yourapp.com"
    override val supportEmail = "support@yourapp.com"
    override val primaryColorArgb = 0xFF1A237EL
    override val onPrimaryColorArgb = 0xFFFFFFFFL
    override val secondaryColorArgb = 0xFF00BCD4L
    override val onSecondaryColorArgb = 0xFF000000L
    override val tertiaryColorArgb = 0xFF4CAF50L
    override val onTertiaryColorArgb = 0xFFFFFFFFL
    override val logoUrl = "https://yourcdn.com/logo.png"   // or "" for no logo
    override val features = FeatureToggles(
        hasHvacCertifications = true,
        hasPlumbingInventory = false,
    )
}
```

4. Run with `-Pclient={name}`.

5. To add a new feature flag, extend `FeatureToggles` in `core:domain` and gate the UI behind `LocalBrandConfig.current.features.yourFlag`.

6. To add the client to the runtime switcher, add an inline object to `demo-brands/src/commonMain/kotlin/com/velsol/demo/DemoBrandConfigs.kt` and append it to `allDemoConfigs`. Update `:brand-parity-tests` if you add parity coverage for the new client.

7. To add the client to CI, append its name to the matrix in `.github/workflows/build-clients.yml`.

> To override the `default` binding in tests or previews, use Metro's `replaces` parameter:
> `@ContributesBinding(AppScope::class, replaces = [DefaultBrandConfig::class])`

---

## Using Brand Config in Composables

Consume `LocalBrandConfig` anywhere inside the Compose tree:

```kotlin
@Composable
fun SomeScreen() {
    val brand = LocalBrandConfig.current
    val features = brand.features

    Text(brand.appName, color = Color(brand.primaryColorArgb))

    if (features.hasHvacCertifications) {
        EpaFormButton()
    }
}
```

`LocalBrandConfig` is provided at the root by `AppTheme`. No parameter threading required.

---

## Adding a New Feature Module

1. Create `features/{name}/build.gradle.kts`:

```kotlin
plugins {
    id("whitelabel.kmp.compose")
}
android { namespace = "com.velsol.feature.{name}" }

compose.resources {
    packageOfResClass = "com.velsol.feature.{name}.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
        }
    }
}
```

2. Register the module in `settings.gradle.kts`: `include(":features:{name}")`.

3. Add it to `sharedUI/build.gradle.kts` under `commonMain.dependencies`.

4. Define a `XxxComponent` interface with a `stack: Value<ChildStack<*, Child>>`, implement `DefaultXxxComponent`, and create `XxxContent` / `XxxDetailContent` composables.

5. Add `val xxx: XxxComponent` to `RootComponent`, instantiate it in `DefaultRootComponent`, and render `Children(component.xxx.stack)` in `RootContent`.

---

## BuildKonfig

`sharedUI/build.gradle.kts` uses BuildKonfig to generate per-build constants:

```kotlin
buildkonfig {
    packageName = "com.velsol"
    val clientId = providers.gradleProperty("client").getOrElse("default")
    defaultConfigs {
        buildConfigField(STRING, "CLIENT_ID", clientId)
        buildConfigField(BOOLEAN, "IS_DEMO_MODE", "true")
    }
}
```

`com.velsol.BuildConfig.CLIENT_ID` and `IS_DEMO_MODE` are available anywhere in `sharedUI` at runtime. Use `IS_DEMO_MODE` to guard the demo switcher in production builds.

---

## CI — Building All Client Flavors

`.github/workflows/build-clients.yml` runs three matrix jobs (`build-android`, `build-desktop`, `build-ios`) for every entry in the `client` list:

```yaml
matrix:
  client: [default, acme, beta, gamma]
```

Each job uploads its artifact under `{client}-android-apk`, `{client}-desktop`, or `{client}-ios-framework`.

---

## Convention Plugin Quick Reference

| Plugin | Targets | What it adds |
|---|---|---|
| `whitelabel.kmp.library` | Android, JVM, iosArm64, iosSimulatorArm64 | JVM 17, compileSdk 36, minSdk 23 |
| `whitelabel.kmp.compose` | (inherits kmp.library) | Compose MP plugins, Compose + Decompose deps (`api`) in commonMain/jvmMain |
| `whitelabel.kmp.database` | (inherits kmp.library) | Room runtime, KSP Room compiler, `schemaDirectory` |
| `whitelabel.kmp.network` | (inherits kmp.library) | Ktor core (api), platform engines, kotlinx-serialization |
| `whitelabel.android.library` | Android only | kotlin.android, JVM 17, compileSdk 36, minSdk 23 |

To change SDK versions or add a universal Compose dependency, edit the relevant file in `build-logic/src/main/kotlin/` — the change propagates to every module automatically.

---

## Demo Client Switcher

The app includes a hidden demo mode for previewing different white-label brand configurations at runtime — no rebuild required.

**To activate:** Triple-tap the app name on the Home screen to open the client switcher.

| Client | Primary Color | Features Enabled |
|---|---|---|
| WhiteLabel App | Purple | (default, no extras) |
| Acme Field Services | Red | HVAC Certifications |
| Beta Plumbing Co | Blue | Plumbing Inventory |
| Gamma Field Tech | Green | HVAC Certifications + Plumbing Inventory |

Switching clients re-themes the entire app and adjusts navigation tabs based on each client's feature flags.

To add a new demo client, add a new `BrandConfig` object in `demo-brands/src/commonMain/kotlin/com/velsol/demo/DemoBrandConfigs.kt` and include it in the `allDemoConfigs` list. Mirror the same fields in `sharedUI/src/commonTest/.../DemoBrandConfigsTest.kt` expected objects, and extend `BrandParityTest` when the real client module exists.

---

## Running the App

### Android
Open the project in Android Studio and run the imported Android run configuration, or:
```bash
./gradlew :androidApp:assembleDebug
# Output: androidApp/build/outputs/apk/debug/androidApp-debug.apk
```

### Desktop
```bash
./gradlew :desktopApp:run
# Hot reload:
./gradlew :desktopApp:hotRun --auto
```

### iOS
Open `iosApp/iosApp.xcproject` in Xcode and run the standard configuration, or use the Kotlin Multiplatform Mobile plugin for Android Studio.
