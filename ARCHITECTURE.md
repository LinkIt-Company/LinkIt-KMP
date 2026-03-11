# LinkIt Company - Architecture

## 📐 프로젝트 구조

이 프로젝트는 **Clean Architecture**와 **멀티모듈** 구조를 기반으로 설계되었습니다.

```
LinkIt-KMP/
├── app-shared/                   # 공유 KMP 모듈 (공통 UI/로직)
├── app-android/                  # Android 애플리케이션 모듈 (진입점)
├── app-ios/                      # iOS 애플리케이션 모듈 (Xcode)
├── core/                         # 공통 모듈
│   ├── common/                   # 공통 유틸리티, 확장 함수, 상수
│   └── designsystem/             # 디자인 시스템 (테마, 컬러, 타이포그래피)
├── domain/                       # 도메인 레이어
│   ├── model/                    # 도메인 모델
│   ├── repository/               # 리포지토리 인터페이스
│   └── usecase/                  # 유스케이스
├── data/                         # 데이터 레이어
│   ├── repository/               # 리포지토리 구현
│   ├── datasource/               # 데이터 소스 (remote, local)
│   ├── dto/                      # Data Transfer Objects
│   └── mapper/                   # DTO to Domain 매퍼
└── feature/                      # 피쳐 모듈
    ├── home/                     # 홈 화면
    ├── classification/           # 분류 화면
    ├── onboarding/               # 온보딩 화면
    ├── save/                     # 저장 화면
    ├── share/                    # 공유 화면
    └── storage/                  # 저장소 화면
```

## 🔗 의존성 규칙

### 허용되는 의존성

```
app-shared → feature-*, domain, data, core-*
app-android → app-shared
feature-* → domain, core-*
domain → core-*
data → domain, core-*
core-designsystem → core-common
```

### 금지되는 의존성

- ❌ **Feature 간 의존**: `feature-A → feature-B`
- ❌ **Domain의 상위 레이어 의존**: `domain → data`, `domain → feature`
- ❌ **Data의 상위 레이어 의존**: `data → feature`
- ❌ **순환 의존**: 모든 순환 의존 금지

## 📦 모듈 설명

### App 모듈

#### app-shared
- 공통 UI 루트 및 앱 조립
- 모든 shared 모듈 의존성 통합
- iOS Framework 산출물 제공

#### app-android
- Android 런처 모듈(애플리케이션 진입점)
- `app-shared` UI/로직 실행

#### app-ios
- iOS 런처 모듈(Xcode 프로젝트)
- `app-shared`에서 생성한 Framework 실행

### Core 모듈

#### core:common
- 공통 유틸리티 함수
- 확장 함수
- 상수
- Result 래퍼 클래스

#### core:designsystem
- Material3 테마
- 컬러 팔레트
- 타이포그래피
- 공통 UI 컴포넌트

### Domain 모듈
- 순수 Kotlin/비즈니스 로직
- 플랫폼 독립적
- Repository 인터페이스 정의
- UseCase 구현
- 도메인 모델 정의

### Data 모듈
- Repository 구현
- 네트워크/로컬 데이터 소스
- DTO 정의
- Domain 모델로의 매핑

### Feature 모듈
각 피쳐 모듈은 독립적인 화면/기능을 담당:
- UI (Compose)
- ViewModel
- Navigation

## 🛠️ Convention Plugins

프로젝트는 Gradle Convention Plugins를 사용하여 빌드 로직을 공유합니다:

- `kmp.shared.convention`: 공유 KMP 앱 모듈(`app-shared`)용
- `android.application.convention`: Android 앱 모듈(`app-android`)용
- `kmp.library.convention`: 라이브러리 모듈용
- `kmp.feature.convention`: 피쳐 모듈용 (Compose 포함)
- `kmp.core.convention`: Core 모듈용

## 🎯 아키텍처 장점

1. **명확한 관심사 분리**: 각 레이어가 고유한 책임을 가짐
2. **테스트 용이성**: 각 모듈을 독립적으로 테스트 가능
3. **확장성**: 새로운 기능을 독립적인 모듈로 추가 가능
4. **빌드 성능**: 변경된 모듈만 재빌드
5. **팀 협업**: 모듈별로 팀을 나눠 작업 가능
6. **플랫폼 독립성**: Domain 레이어는 플랫폼 독립적

## 🚀 개발 가이드

### 새로운 Feature 추가하기

1. `feature/` 디렉토리에 새 모듈 생성
2. `settings.gradle.kts`에 모듈 추가
3. `build.gradle.kts`에 `kmp.feature.convention` 플러그인 적용
4. `app-shared/build.gradle.kts`에 의존성 추가

### 새로운 UseCase 추가하기

1. `domain/usecase/`에 UseCase 클래스 생성
2. 필요한 경우 Repository 인터페이스 정의
3. `data/repository/`에 구현 추가

### 공통 UI 컴포넌트 추가하기

1. `core/designsystem/`에 컴포넌트 추가
2. 모든 feature 모듈에서 사용 가능

## 📚 참고 자료

- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
