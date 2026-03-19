# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew assembleDebug              # Build debug APK
./gradlew assembleRelease            # Build release APK
./gradlew assembleFreeDebug          # Free flavor debug APK
./gradlew assembleProprietaryDebug   # Proprietary flavor debug APK
./gradlew test                       # Run all unit tests
./gradlew testDebugUnitTest          # Run debug unit tests only
./gradlew lint                       # Run lint checks (non-blocking, abortOnError false)
./gradlew clean                      # Clean build artifacts
```

## Architecture

The app follows a strict layered architecture:

**Presentation → Controller → Repository → Database (SQLite)**

- **`activity/`** — Android Activities (UI entry points). All extend `BaseActivity` or `BaseDrawerActivity`.
- **`controller/`** — Business logic layer. Controllers consume repos and expose domain operations (e.g., `RecordController`, `AccountController`, `CurrencyController`).
- **`repo/`** — Repository pattern. `IRepo<T>` interface defines CRUD; `BaseRepo<T>` provides SQLite implementation. `CachedRepoModule` (Dagger) wires up cached repo instances.
- **`entity/`** — Data models: `Account`, `Record`, `Category`, `Transfer`, `ExchangeRate`, `Period`.
- **`adapter/`** — RecyclerView/ListView adapters.
- **`ui/presenter/`** — Presenters for complex UI logic (`AccountsSummaryPresenter`, `ShortSummaryPresenter`).
- **`report/`** — Report and chart generation (`ReportMaker`, `MonthReport`, `BarChartConverter`).
- **`di/`** — Dagger 2 configuration. Entry point: `AppComponent` built in `MtApp`.

## Dependency Injection

Dagger 2 is used throughout. The component is built in `MtApp` (Application class) with two modules:
- `CachedRepoModule` — provides repository instances
- `ControllerModule` — provides controller instances

Activities inject controllers directly. New classes that need DI must be added to the appropriate Dagger module.

## Key Patterns

- **View Binding** is enabled globally — use `ActivityXxxBinding` / `FragmentXxxBinding`, not `findViewById`.
- **`IRepo<T>`** is the contract for all data access. Implement it (or extend `BaseRepo`) for new entity types.
- **`Record`** uses `TYPE_INCOME = 0` / `TYPE_EXPENSE = 1` integer constants.
- **`MtApp.instance`** provides the singleton application instance.
- **Timber** is used for logging (`Timber.d(...)`, `Timber.e(...)`), not `Log`.

## Testing

Tests are in `app/src/test/` and use JUnit 4 + Mockito. The pattern is to mock dependencies (controllers, repos) and test business logic in isolation. See `CurrencyControllerTest`, `RecordControllerTest`, etc. for examples.

## Java → Kotlin Migration

The codebase is actively migrating from Java to Kotlin (branch `feature/38-convert-java-to-kotlin`). New code should be written in Kotlin. When converting Java files, maintain the same class/function names and behavior — do not refactor logic during conversion.

## Product Flavors

Two flavors: `free` and `proprietary`. Firebase/Crashlytics integration exists in both; the proprietary flavor may include additional features. Flavor-specific source sets are in `app/src/free/` and `app/src/proprietary/`.

## Libraries

- **Dagger 2** — dependency injection
- **MPAndroidChart** — charts and visualizations
- **Timber** — logging
- **Dropbox Core SDK** — backup/restore
- **Firebase Analytics + Crashlytics** — crash reporting (disabled in debug builds)
- **Kotlin Coroutines** — async operations
