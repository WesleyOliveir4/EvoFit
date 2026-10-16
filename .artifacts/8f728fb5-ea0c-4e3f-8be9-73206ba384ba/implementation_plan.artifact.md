# KMP Migration - Phase 1: Shared Module Structure Setup

This plan outlines the steps for setting up the Kotlin Multiplatform (KMP) `:shared` module and configuring Gradle to support multiplatform targets (Android and iOS), keeping the existing Android app functioning.

## User Review Required

> [!IMPORTANT]
> This phase establishes the foundation of the KMP architecture by creating the `:shared` module, configuring Kotlin Multiplatform plugins, and preparing iOS framework generation targets without altering existing Android flows.

## Open Questions

- None at this stage.

## Proposed Changes

### Gradle & Project Configuration

#### [MODIFY] [libs.versions.toml](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFitKMP/EvoFit/gradle/libs.versions.toml)
- Add Kotlin Multiplatform plugin version/alias if needed.
- Add common multiplatform libraries (coroutines, serialization, etc.).

#### [MODIFY] [settings.gradle.kts](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFitKMP/EvoFit/settings.gradle.kts)
- Include `:shared` module.

#### [NEW] [build.gradle.kts](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFitKMP/EvoFit/shared/build.gradle.kts)
- Configure KMP targets (`androidTarget`, `iosX64`, `iosArm64`, `iosSimulatorArm64`).
- Configure Kotlin Multiplatform plugin and serialization/coroutines dependencies in `commonMain`, `androidMain`, and `iosMain`.

#### [NEW] [AndroidManifest.xml](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFitKMP/EvoFit/shared/src/androidMain/AndroidManifest.xml)
- Basic Android manifest for the shared module.

#### [NEW] [Platform.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFitKMP/EvoFit/shared/src/commonMain/kotlin/com/evofit/shared/Platform.kt)
- Sample multiplatform expected/actual or shared helper class to verify compilation.

## Verification Plan

### Automated Tests
- Run `./gradlew :shared:compileKotlinCommon` or `./gradlew build` via gradle build to ensure the shared module compiles correctly for both Android and iOS targets.

### Manual Verification
- Verify Android app (`:app`) builds and runs normally.
