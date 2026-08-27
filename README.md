# CoinMarketCap Android

Android app that lists cryptocurrency exchanges using the CoinMarketCap API, built with Jetpack Compose and clean architecture.

## Tech Stack

- **UI:** Jetpack Compose + Material 3
- **Navigation:** Navigation Compose
- **DI:** Hilt
- **Network:** Retrofit + OkHttp
- **Image loading:** Coil 3
- **Async:** Kotlin Coroutines + Flow
- **Architecture:** MVVM + Clean Architecture (UseCase / Repository / DataSource)

## Project Structure

```
app/
├── data/
│   ├── datasource/       # DataSource interface and implementation
│   ├── remote/           # Retrofit API services and response models
│   └── repository/       # Repository implementation
├── domain/
│   ├── models/           # Domain entities
│   ├── repository/       # Repository interface
│   └── usecase/          # Use cases
├── ui/
│   ├── features/
│   │   ├── home/         # Home screen (exchange list)
│   │   └── detail/       # Detail screen (exchange info + assets)
│   ├── nav/              # Navigation host and routes
│   └── viewmodel/        # ViewModels
└── utils/                # Extensions, UiState, ApiState, DispatchersProvider
```

## Running Tests

```bash
./gradlew testDebugUnitTest
```

## Code Coverage

```bash
./gradlew jacocoTestReport
# Report: app/build/reports/jacoco/jacocoTestReport/html/index.html
```

## Scalability Analysis

### What favors scalability

- **Well-defined architecture:** The separation into layers (`data` → `domain` → `ui`) is correct. Use cases isolate business rules, the repository abstracts the data source, and the UI only knows the ViewModel. Adding a new feature follows a clear path.
- **Hilt for DI:** Makes it easy to swap implementations without changing consumer code. The `TestRepositoryModule` created in tests already proves dependency inversion works.
- **Compose + StateFlow:** Unidirectional reactivity (ViewModel → UI) is predictable and easy to scale with new screens.
- **98.9% test coverage:** Future refactors have a solid safety net.

### What limits scalability today

- **Single Gradle module (`:app`):** Everything is in one module. As the project grows, build times increase and responsibility boundaries blur. The ideal would be to modularize into `:feature:home`, `:feature:detail`, `:core:network`, `:core:domain`.
- **`AppNavHost` coupled to features:** All navigation goes through a single file. With more screens this becomes a maintenance bottleneck. The recommended pattern is modular navigation with nested graphs.
- **`CryptocurrencyRepositoryImpl` with API combination logic:** The repository manually joins two calls (`mapCoins` + `exchangeInfo`). This logic should live in the use case or a dedicated mediator.
- **Partially externalized strings:** Some strings are still hardcoded, which hinders future internationalization.
- **`safeApiCall` as a global function:** Works, but in a larger project the ideal is to encapsulate it in a class to allow per-feature override.

### Summary

| Aspect | Status |
|---|---|
| Layered architecture | Good |
| Dependency injection | Good |
| Test coverage | Excellent |
| Gradle modularization | Absent |
| Scalable navigation | Limited |
| Internationalization | Partial |

The project has a **solid foundation to scale**, but would need Gradle modularization before growing to 5+ features without losing organization and build speed.

---

### Coverage Report

| File | Lines Covered | Coverage |
|---|---|---|
| `CryptocurrencyMapUseCase.kt` | 3/3 | 100% |
| `ExchangeAssetsUseCase.kt` | 3/3 | 100% |
| `CryptocurrencyRepositoryImpl.kt` | 44/44 | 100% |
| `CryptocurrencyDataSourceImpl.kt` | 6/6 | 100% |
| `ApiState.kt` | 20/20 | 100% |
| `UiState.kt` | 4/4 | 100% |
| `CryptocurrencyMapResponse.kt` | 17/17 | 100% |
| `ExchangeInfoResponse.kt` | 24/24 | 100% |
| `ExchangeAssetsResponse.kt` | 20/20 | 100% |
| `CoinWithExchangeInfo.kt` | 17/17 | 100% |
| `DoubleExtensions.kt` | 3/3 | 100% |
| `StringExtensions.kt` | 7/7 | 100% |
| `ExchangeAssetEntity.kt` | 3/3 | 100% |
| `DispatchersProvider.kt` | 4/5 | 80% |
| `MapCoinsEntity.kt` | 12/13 | 92.3% |
| **Total** | **187/189** | **98.9%** |
