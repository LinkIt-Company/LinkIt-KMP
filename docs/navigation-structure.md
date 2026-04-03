# Navigation3 구조 정리

## 모듈 의존성 그래프

```mermaid
graph TD
    App[":app"]
    CoreNav[":core:navigation"]
    CoreCommon[":core:common"]
    CoreDS[":core:designsystem"]
    Domain[":domain"]
    Data[":data"]
    FHome[":feature:home"]
    FOnboarding[":feature:onboarding"]
    FSave[":feature:save"]
    FShare[":feature:share"]
    FStorage[":feature:storage"]
    FClassification[":feature:classification"]

    App --> CoreNav
    App --> CoreCommon
    App --> CoreDS
    App --> Domain
    App --> Data
    App --> FHome
    App --> FOnboarding
    App --> FSave
    App --> FShare
    App --> FStorage
    App --> FClassification
    CoreNav --> CoreCommon

    FHome --> CoreCommon
    FHome --> CoreDS
    FHome --> Domain
    FOnboarding --> CoreCommon
    FOnboarding --> CoreDS
    FOnboarding --> Domain
    FSave --> CoreCommon
    FSave --> CoreDS
    FSave --> Domain
    FShare --> CoreCommon
    FShare --> CoreDS
    FShare --> Domain
    FStorage --> CoreCommon
    FStorage --> CoreDS
    FStorage --> Domain
    FClassification --> CoreCommon
    FClassification --> CoreDS
    FClassification --> Domain

    Data --> CoreCommon
    Data --> Domain
```

## 화면 네비게이션 플로우

```mermaid
graph LR
    Home["Home<br/>(Start)"]
    Onboarding["Onboarding"]
    Save["Save"]
    Share["Share"]
    Storage["Storage"]
    Classification["Classification"]

    Home -->|navigate| Onboarding
    Home -->|navigate| Save
    Home -->|navigate| Share
    Home -->|navigate| Storage
    Home -->|navigate| Classification

    Onboarding -->|navigateAndClearStack| Home
    Onboarding -->|popBack| Home
    Save -->|popBack| Home
    Share -->|popBack| Home
    Storage -->|popBack| Home
    Classification -->|popBack| Home
```

## 핵심 컴포넌트 구조

```mermaid
graph TB
    subgraph ":core:navigation"
        LinkItRoute["LinkItRoute<br/>(sealed interface)"]
        LinkItNavigator["LinkItNavigator<br/>navigate() / popBack() / navigateAndClearStack()"]
        LinkItNavDisplay["LinkItNavDisplay<br/>CompositionLocalProvider + NavDisplay"]
        LocalNav["LocalLinkItNavigator<br/>(CompositionLocal)"]

        LinkItNavDisplay --> LocalNav
        LocalNav --> LinkItNavigator
        LinkItNavigator --> LinkItRoute
    end

    subgraph ":app"
        LinkItApp["LinkItApp"]
        EntryProvider["linkItEntryProvider<br/>Route → Screen 매핑"]

        LinkItApp --> LinkItNavDisplay
        LinkItApp --> EntryProvider
    end

    subgraph ":feature:*"
        Screens["HomeScreen / OnboardingScreen / ...<br/>(콜백 람다만 수신, Nav 의존성 없음)"]
    end

    EntryProvider --> Screens
    EntryProvider --> LocalNav
```

## 주요 파일 경로

| 파일　　　　　　　　　　　　　　　　　　　　| 역할　　　　　　　　　　　　　　　　　　　　 |
| ---------------------------------------------| ----------------------------------------------|
| `core/navigation/.../LinkItRoute.kt`　　　　| `@Serializable sealed interface` 라우트 정의 |
| `core/navigation/.../LinkItNavigator.kt`　　| 백스택 래퍼 (안전한 API만 노출)　　　　　　　|
| `core/navigation/.../LinkItNavDisplay.kt`　 | NavDisplay + CompositionLocal 제공　　　　　 |
| `app/.../navigation/LinkItEntryProvider.kt` | Route → Screen 매핑 (entryProvider DSL)　　　|
| `app/.../LinkItApp.kt`　　　　　　　　　　　| 진입점 (Theme + NavDisplay 조합)　　　　　　 |
| `feature/*/.../*Screen.kt`　　　　　　　　　| 순수 UI 컴포저블 (콜백 람다)　　　　　　　　 |

## LinkItNavigator API

| 메서드 | 동작 | 안전장치 |
|--------|------|----------|
| `navigate(route)` | 백스택에 추가 | - |
| `popBack()` | 마지막 항목 제거 | `size > 1` 가드 (빈 스택 방지) |
| `navigateAndClearStack(route)` | 스택 초기화 후 새 루트 설정 | `Snapshot.withMutableSnapshot` 원자적 처리 |
