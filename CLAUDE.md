# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

LinkIt-KMP is a Kotlin Multiplatform (KMP) project targeting Android and iOS, built with Compose Multiplatform and Clean Architecture.

## Build Commands

```shell
# Android
./gradlew :app:assembleDebug

# Full build check
./gradlew build

# Single module build
./gradlew :feature:home:build
./gradlew :core:navigation:build
```

iOS is built via Xcode from the `iosApp/` directory.

## Architecture

**Clean Architecture with multi-module structure.** Dependency rules are strict:

```
app → feature-*, domain, data, core-*
feature-* → domain, core-* (NO inter-feature dependencies)
data → domain, core-*
domain → core-common (pure Kotlin, platform-independent)
core:designsystem → core:common
core:navigation → core:common
```

### Module Roles

- **app**: Entry point, wires navigation routes to feature screens via `LinkItEntryProvider`
- **core:navigation**: Navigation3 wrapper (`LinkItRoute`, `LinkItNavigator`, `LinkItNavDisplay`)
- **core:designsystem**: Material3 theme (colors, typography)
- **core:common**: `Result` sealed class, constants, utilities
- **domain**: Business logic, repository interfaces, use cases
- **data**: Repository implementations, data sources, DTOs, mappers
- **feature:***: Pure composable screens that accept callback lambdas (no direct navigation dependency)

### Navigation Pattern (Navigation3)

Routes are defined as `@Serializable` sealed interface members in `core/navigation/.../LinkItRoute.kt`. Route-to-screen mapping is done in `app/.../navigation/LinkItEntryProvider.kt` using the `entryProvider` DSL. Screens receive navigation callbacks via `LocalLinkItNavigator` (CompositionLocal), never depending on navigation directly.

`LinkItNavigator` wraps `SnapshotStateList<LinkItRoute>` and exposes: `navigate(route)`, `popBack()`, `navigateAndClearStack(route)`.

### Platform Entry Points

- **Android**: `IntroActivity` (launcher) → `MainActivity` which calls `LinkItApp()`
- **iOS**: `IntroViewController` / `MainViewController` via `ComposeUIViewController`, wrapped in SwiftUI via `ContentView.swift`

## Build System

Convention plugins in `build-logic/conventions/` provide shared config:
- `kmp.application.convention` — app module
- `kmp.library.convention` — library modules
- `kmp.feature.convention` — feature modules (adds Compose dependencies)
- `kmp.core.convention` — core modules

Targets: `androidTarget` (JVM 11, compileSdk 36, minSdk 24), `iosArm64`, `iosSimulatorArm64`.

## Adding a New Feature

1. Create module under `feature/`
2. Add to `settings.gradle.kts`
3. Apply `kmp.feature.convention` plugin in its `build.gradle.kts`
4. Add dependency in `app/build.gradle.kts`
5. Define route in `LinkItRoute.kt`, map it in `LinkItEntryProvider.kt`

## Language

Project documentation and commit messages are in Korean. Code (identifiers, comments) is in English.
