# CLAUDE.md

This file provides guidance to Claude Code when working with this repository.

## Teaching Mode

**This is a teaching/learning project.** Claude should:
- Guide step-by-step, explaining the "why" behind each decision
- Provide code snippets with explanations
- Let the user create files, folders, packages, and type the code themselves
- Act as a tutor, not an auto-coder

## Project Overview

SkillForgeAI is an on-device ML-powered learning progression app built with Kotlin and Jetpack Compose. Users create skills, study flashcards, and progress through Bloom's Taxonomy levels (Remember → Understand → Apply → Analyze → Evaluate → Create) based on AI-driven recommendations.

- **Package**: `com.example.skillforge`
- **Min SDK**: 26 (Android 8.0)
- **Target/Compile SDK**: 35
- **Build System**: Gradle with Kotlin DSL
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture (UseCases)
- **DI Framework**: Hilt
- **Database**: Room with KSP
- **ML Runtime**: TensorFlow Lite (on-device)

## Build Commands

  ```bash
  # Build the project
  ./gradlew build

  # Build debug APK
  ./gradlew assembleDebug

  # Install debug build on connected device/emulator
  ./gradlew installDebug

  # Clean build artifacts
  ./gradlew clean

  # Check for dependency updates
  ./gradlew dependencyUpdates

  Testing Commands

  # Run all unit tests
  ./gradlew test

  # Run unit tests for debug variant
  ./gradlew testDebugUnitTest

  # Run instrumented tests (requires device/emulator)
  ./gradlew connectedAndroidTest

  # Run specific test class
  ./gradlew test --tests "com.example.skillforge.ExampleTest"

  Architecture

  Package Structure

  com.example.skillforge/
  ├── data/
  │   ├── model/           # Entity classes (Skill, Flashcard, Activity, UserProgress)
  │   ├── repository/      # Repository interfaces and implementations
  │   ├── local/
  │   │   ├── dao/         # Room DAOs
  │   │   └── converter/   # Room type converters
  │   └── starter/         # Pre-loaded content (Drummer pack)
  ├── ml/
  │   ├── model/           # TFLite model wrappers
  │   ├── feature/         # Feature extraction
  │   ├── inference/       # Interpreter manager, delegates
  │   ├── versioning/      # Model version compatibility
  │   ├── scheduling/      # Inference caching, WorkManager
  │   └── instrumentation/ # Performance tracking
  ├── domain/
  │   ├── usecase/         # Business logic use cases
  │   └── engine/          # Progression engine, threshold evaluator
  ├── ui/
  │   ├── navigation/      # Compose Navigation
  │   ├── screens/         # Screen composables
  │   ├── components/      # Reusable UI components
  │   ├── viewmodel/       # ViewModels
  │   └── theme/           # Material 3 theming
  └── di/                  # Hilt modules

  Data Flow

  User Action → Screen → ViewModel → UseCase → Repository → Room/ML
                  ↑                                ↓
                  ←──────── StateFlow ←───────────┘

  Bloom's Taxonomy Levels

  | Level      | Order | Advance Threshold    | Demote Threshold      |
  |------------|-------|----------------------|-----------------------|
  | REMEMBER   | 1     | 80% + 5 activities   | <50% for 3 days       |
  | UNDERSTAND | 2     | 75% + 3 explanations | <40%, 7 days inactive |
  | APPLY      | 3     | 70% on 5 tasks       | <50% task success     |
  | ANALYZE    | 4     | 3 analyses done      | 3 failed attempts     |
  | EVALUATE   | 5     | 2 evaluations        | 14 days inactive      |
  | CREATE     | 6     | 1 project            | N/A (mastery)         |

  ML Architecture

  Production Patterns Implemented

  1. Model Versioning (ml/versioning/)
    - modelVersion and featureSchemaVersion tracking
    - Compatibility checking before inference
  2. Delegate Fallback (ml/inference/MLInterpreterManager.kt)
    - Try NNAPI → GPU → CPU fallback chain
    - Singleton interpreter to avoid reload overhead
  3. Inference Scheduling (ml/scheduling/)
    - Debounced triggers (2s default)
    - WorkManager for background evaluation
    - Result caching with stale marking
  4. Performance Instrumentation (ml/instrumentation/)
    - Timing around feature extraction + inference
    - Slow path warnings (>100ms yellow, >500ms red)

  Feature Schema (v1)

  17 features extracted from user activity:
  - Temporal: daysSinceLastActivity, activitiesLast7Days, streakDays, etc.
  - Performance: averageScoreLast5, scoreTrend, consecutiveFailures, etc.
  - Level: currentLevelOrdinal,ou a daysAtCurrentLevel, etc.
  - Engagement: completionRate, voluntaryPracticeRatio, etc.

  Key Dependencies

  // Core
  hilt = "2.51"
  room = "2.6.1"
  compose-bom = "2024.02.00"

  // ML
  tensorflow-lite = "2.14.0"
  tensorflow-lite-support = "0.4.4"
  tensorflow-lite-gpu = "2.14.0"

  // Utilities
  kotlinx-datetime = "0.5.0"
  kotlinx-serialization = "1.6.2"
  datastore = "1.0.0"
  coil = "2.5.0"

  Starter Content

  The app ships with a "Drumming Fundamentals" skill containing ~25 flashcards across all 6 Bloom levels. Cards include:
  - Rudiment definitions (paradiddle, flam, drag)
  - Notation reading (note values, time signatures)
  - Practice exercises with tempo targets
  - Analysis comparisons
  - Creative composition prompts

  Media assets stored in assets/drumming/ (images, audio clips).

  Development Notes

  - Always run ./gradlew build before committing
  - Room schemas exported to schemas/ for migration testing
  - TFLite models stored in assets/ml/
  - Use @VisibleForTesting for test-only accessors
  - Prefer StateFlow over LiveData for new code

---

## Tutorial Progress

This project is being built as a guided 15-session tutorial. Below is the current progress.

### Session 1: Project Setup & Theming ✓

| Task | Status |
|------|--------|
| New Android project "SkillForge" | ✓ Done |
| build.gradle.kts with Hilt + KSP | ✓ Done |
| CLAUDE.md with project instructions | ✓ Done |
| SkillForgeApplication.kt with @HiltAndroidApp | ✓ Done |
| MainActivity with @AndroidEntryPoint | ✓ Done |
| Drumming-inspired theme (crimson, amber, charcoal) | ✓ Done |
| Checkpoint: App shows "SkillForge" with themed UI | ✓ Done |

### Session 2: Data Models & Room Database ✓

| Task | Status |
|------|--------|
| Room dependencies in version catalog | ✓ Done |
| LearningLevel enum (Bloom's Taxonomy) | ✓ Done |
| Skill, Flashcard, Activity, UserProgress entities | ✓ Done |
| SkillDao, FlashcardDao, ActivityDao, ProgressDao | ✓ Done |
| Type Converters + AppDatabase | ✓ Done |
| Tests: 55 total (16 unit + 39 instrumented) | ✓ All passing |

### Session 3: Repository Layer & DI ✓

| Task | Status |
|------|--------|
| Create SkillRepository interface + impl | ✓ Done |
| Create FlashcardRepository interface + impl | ✓ Done |
| Create ActivityRepository interface + impl | ✓ Done |
| Create ProgressRepository interface + impl | ✓ Done |
| Create DatabaseModule.kt (provides Room) | ✓ Done |
| Create RepositoryModule.kt (binds implementations) | ✓ Done |
| Repository tests (26 new instrumented tests) | ✓ All passing |
| Tests: 64 total instrumented | ✓ All passing |

**Concepts learned:**
- Why repositories wrap DAOs (abstraction, testability, flexibility)
- Hilt `@Module`, `@Provides`, `@Binds`
- `@Singleton` scope
- Interface vs implementation separation
- `@InstallIn(SingletonComponent::class)` for app-lifetime dependencies

### Session 4: Drummer Starter Pack & Content Seeding ✓

| Task | Status |
|------|--------|
| Create DrummerStarterContent.kt with flashcard data | ✓ Done |
| Create ContentSeeder.kt to populate database | ✓ Done |
| Add seeding to app startup (via Hilt) | ✓ Done |
| Checkpoint: App launches with pre-loaded drumming skill | ✓ Done |

**Concepts learned:**
- Database seeding strategies (check-then-insert pattern)
- Kotlin `object` for static singleton data
- `@Inject lateinit var` for field injection in Android-managed classes
- `CoroutineScope(Dispatchers.IO)` for off-main-thread startup work
- Separating content definition from persistence (FlashcardData vs Flashcard)

### Session 5: Home & Dashboard UI ✓

| Task | Status |
|------|--------|
| Add hilt-navigation-compose + lifecycle-runtime-compose deps | ✓ Done |
| Create HomeViewModel with combined skill + progress state | ✓ Done |
| Create SkillCard reusable composable component | ✓ Done |
| Create HomeScreen with loading/empty/content states | ✓ Done |
| Wire HomeScreen into MainActivity | ✓ Done |
| Checkpoint: App shows dashboard with seeded drumming skill | ✓ Done |

**Concepts learned:**
- `@HiltViewModel` + `@Inject constructor` for ViewModel DI
- `StateFlow` + `combine` to merge multiple data sources
- `collectAsStateWithLifecycle()` for lifecycle-aware observation
- `LazyColumn` with `key` for efficient lists
- `when` pattern for handling UI states (loading/empty/content)
- Separating reusable components from screens

### Session 6: Flashcard System ✓

| Task | Status |
|------|--------|
| Add navigation-compose dependency | ✓ Done |
| Create FlashcardStudyViewModel with session state | ✓ Done |
| Create FlashcardScreen with 3D flip animation | ✓ Done |
| Add scoring UI (correct/incorrect buttons) | ✓ Done |
| Create session completion screen with stats | ✓ Done |
| Set up Compose Navigation (NavHost, routes) | ✓ Done |
| Navigate from HomeScreen to study session | ✓ Done |
| Checkpoint: User can study flashcards and record scores | ✓ Done |

**Concepts learned:**
- Compose Navigation: `NavHost`, `NavController`, route parameters
- `SavedStateHandle` to receive navigation arguments in ViewModels
- `animateFloatAsState` for smooth animations
- `graphicsLayer { rotationY }` for 3D card flip effect
- `cameraDistance` to prevent distortion during rotation
- Counter-rotating content so text reads correctly when flipped
- `MutableStateFlow` for user-interaction-driven state management

### Session 7: Activity Tracking ← UP NEXT

| Task | Status |
|------|--------|
| Record Activity on flashcard answer | Pending |
| Update UserProgress after study session | Pending |
| Add streak tracking logic | Pending |
| Show activity stats on HomeScreen | Pending |
| Checkpoint: App tracks and displays user activity | Pending |

**Concepts to learn:**
- Recording user activity to database
- Updating progress metrics
- Date/time handling for streaks

### Sessions 8-15: Remaining
8. Progression Engine (Rules-Based)
9. ML Infrastructure
10. Feature Engineering
11. Train & Export TFLite Model
12. ML Integration & Hybrid Engine
13. Recommendations UI
14. Polish & UX
15. Testing & Wrap-up
