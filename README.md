# LinkIt Company

This is a Kotlin Multiplatform project targeting Android, iOS using Clean Architecture.

## Architecture

This project follows **Clean Architecture** with a **multi-module** structure:

- **app**: Application entry point
- **core**: Common utilities and design system
  - `core:common`: Utilities, extensions, constants
  - `core:designsystem`: Theme, colors, typography
- **domain**: Business logic layer (platform-independent)
- **data**: Data layer (repositories, data sources)
- **feature**: Feature modules
  - `feature:home`, `feature:classification`, `feature:onboarding`
  - `feature:save`, `feature:share`, `feature:storage`

**For detailed architecture explanation, see [ARCHITECTURE.md](./ARCHITECTURE.md)**

## Project Structure

* [/app](./app/src) is the application module that integrates all features.
  - [commonMain](./app/src/commonMain/kotlin) is for code that's common for all targets.
  - Platform-specific folders contain platform-specific implementations.

* [/core](./core) contains shared modules used across the entire project.

* [/domain](./domain) contains business logic and repository interfaces.

* [/data](./data) contains data sources and repository implementations.

* [/feature](./feature) contains feature modules with UI and ViewModels.

* [/iosApp](./iosApp/iosApp) contains iOS application entry point.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE's toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :app:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :app:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE's toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
