$ARGUMENTS 의 Figma URL들을 분석하여, 동일 화면의 여러 상태에 대한 MVI 기반 Compose UI 코드를 구현한다.

## 진행 순서

### 0. URL 파싱 및 멀티 URL 설정

- `$ARGUMENTS`를 공백으로 분리하여 여러 Figma URL을 추출한다.
- 각 URL에서 `fileKey`와 `nodeId`를 추출한다 (nodeId의 `-`는 `:`로 변환).
- URL이 1개인 경우에도 동일한 워크플로우로 진행한다 (상태가 1개인 화면으로 취급).
- 임시 인덱스(State 1, State 2, ...)를 부여하고, 1단계에서 상태 이름을 확정한다.

### 1. Figma 디자인 분석 + 상태 추론

`figma:figma-implement-design` 스킬을 사용하여 디자인 컨텍스트를 가져온다.

1. **모든 URL**에 대해 `get_design_context`를 호출하여 디자인 정보와 스크린샷을 가져온다.
2. `docs/SITEMAP.md`를 읽고, Figma 디자인과 대조하여 **해당 화면이 어떤 스크린인지** 식별한다.
3. 각 URL의 시각적 차이를 분석하여 **상태 이름을 자동 부여**한다:
   - 콘텐츠 유무 → Default vs Empty
   - 로딩 인디케이터 존재 → Loading
   - 에러 메시지/재시도 버튼 → Error
   - 인터랙션 차이 → Selected, Expanded, Filtered 등
   - 상태명은 영문 PascalCase로 작성한다 (예: `Default`, `Loading`, `Empty`, `Error`)
4. 결과물: `screenName`(화면 이름) + `states[]`(상태 리스트) 확정
   ```
   screenName = "Xxx"
   states = [
     { name: "Default", fileKey: "...", nodeId: "...", designContext: {...} },
     { name: "Loading", fileKey: "...", nodeId: "...", designContext: {...} },
     { name: "Empty",   fileKey: "...", nodeId: "...", designContext: {...} },
   ]
   ```

### 2. 디자인 토큰 매핑

`docs/DESIGN_TOKEN_MAPPING.md`를 읽고, Figma 변수를 프로젝트 토큰에 매핑한다.
- `Color.kt`, `Type.kt`, `Shape.kt`의 기존 토큰을 우선 사용한다
- 매핑되지 않는 새 토큰이 있으면 직접 판단하여 토큰 파일에 추가한다
- 모든 상태에 공통으로 적용한다

### 3. 기존 컴포넌트 활용

`core/designsystem/src/commonMain/kotlin/.../component/` 디렉토리의 기존 컴포넌트를 최대한 활용하여 구현한다.
- LinkItButton, LinkItTag, LinkItTextField, LinkItTopAppBar, LinkItFilterChip 등
- 새 컴포넌트가 필요한 경우 기존 컴포넌트 패턴을 따라 작성한다
- 모든 상태에 공통으로 적용한다

### 4. 아이콘 처리

**모든 상태**의 디자인에서 아이콘을 수집하고, 중복을 제거한 뒤 일괄 처리한다.

- `get_design_context` 결과에서 아이콘 에셋을 식별한다 (data-name 속성에 아이콘 이름이 표시됨)
- **아이콘 식별 기준**: `<img>` 태그로 렌더링되고, 크기가 작은(24dp 이하) 요소는 아이콘으로 판단한다
- **`FIGMA_TOKEN` 사전 확인**: 아이콘이 1개라도 식별되면 즉시 `source ~/.zshrc && echo $FIGMA_TOKEN`으로 토큰 존재 여부를 확인한다. 없으면 `~/.zshrc`에서 로드를 재시도한다. 그래도 없으면 Material 아이콘 대체로 진행한다 (사용자에게 묻지 않는다).
- **아이콘 처리 우선순위** (Figma 원본 다운로드를 기본으로 한다):
  1. **기존 다운로드 확인**: `core/designsystem/src/commonMain/composeResources/drawable/`에 동일 이름(`ic_{아이콘명}.xml`)의 파일이 이미 있으면 그대로 재활용한다.
  2. **Figma에서 SVG 다운로드** (기본 방식): 기존 파일이 없으면 아래 절차로 Figma에서 직접 가져온다.
  3. **Material 아이콘 대체** (최후 수단): `FIGMA_TOKEN`이 없는 경우에만, `get_screenshot`으로 Figma 아이콘 스크린샷을 찍고 Material Icons와 **시각적으로 비교**하여 형태가 동일한 경우에 한해 대체한다. 이름만 같고 형태가 다르면 대체하지 않는다.
- **Figma SVG 다운로드 절차**:
  1. 아이콘 노드의 nodeId를 확인한다 (data-node-id 속성)
  2. Figma API로 SVG를 다운로드한다:
     ```
     curl -s -H "X-Figma-Token: $FIGMA_TOKEN" \
       "https://api.figma.com/v1/images/{fileKey}?ids={nodeId}&format=svg" \
       | python3 -c "import sys,json; print(list(json.load(sys.stdin)['images'].values())[0])"
     ```
     반환된 URL에서 SVG 파일을 다운로드한다.
  3. SVG를 Android Vector Drawable XML로 변환한다:
     ```
     python3 scripts/svg2vector.py input.svg output.xml
     ```
     스크립트가 없으면 수동으로 변환하거나, 간단한 아이콘은 path 데이터를 직접 추출한다.
  4. 변환된 XML을 `core/designsystem/src/commonMain/composeResources/drawable/` 에 `ic_{아이콘명}.xml`로 저장한다
  5. `LinkItIcons`에 Compose Resource로 등록한다:
     ```kotlin
     val CustomIcon: DrawableResource get() = Res.drawable.ic_custom_icon
     ```
- 여러 아이콘의 nodeId를 쉼표로 연결하여 한 번의 API 호출로 일괄 다운로드한다:
  ```
  curl -s -H "X-Figma-Token: $FIGMA_TOKEN" \
    "https://api.figma.com/v1/images/{fileKey}?ids={nodeId1},{nodeId2},{nodeId3}&format=svg"
  ```
- 이미지(사진, 썸네일 등)는 아이콘이 아니므로 플레이스홀더로 대체한다.

### 5. MVI 아키텍처 생성

CLAUDE.md의 참고 문서 지침에 따라 `docs/ARCHITECTURE.md`, `docs/METRO_INSTRUCTION.md`를 읽고 기존 패턴(`feature/home/sample/` 참조)을 따른다.

**5a. XxxUiState.kt 생성**

Figma 상태 간 시각적 차이를 분석하여 UiState 필드를 도출한다:
- 로딩 스피너가 있는 상태 vs 콘텐츠가 있는 상태 → `isLoading: Boolean`
- 빈 화면 일러스트 → 리스트/콘텐츠 필드가 비어있을 때 자동 처리
- 에러 메시지/재시도 → `errorMessage: String?`
- 데이터가 있는 "Default" 상태에서 콘텐츠 필드의 타입을 결정

```kotlin
data class XxxUiState(
    val isLoading: Boolean,
    val items: List<...>,
    val errorMessage: String?,
    // ... 상태 차이에서 도출된 필드
) : UiState {
    companion object {
        val INITIAL_STATE = XxxUiState(...)
    }
}
```

**5b. XxxIntent.kt 생성**

SITEMAP.md의 사용자 액션 목록과 Figma 인터랙션 요소에서 Intent를 도출한다:
```kotlin
sealed interface XxxIntent : Intent {
    data object Initialize : XxxIntent
    // SITEMAP.md 액션 + Figma 버튼/인터랙션에서 도출
}
```

**5c. XxxSideEffect.kt 생성**

화면 전환, 토스트 등 일회성 이벤트를 정의한다:
```kotlin
sealed interface XxxSideEffect : SideEffect {
    // Navigation, Toast 등
}
```

**5d. XxxViewModel.kt 생성**

`feature/home/sample/HomeViewModel.kt` 패턴을 정확히 따른다:
```kotlin
@AssistedInject
class XxxViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
) : ViewModel(),
    PopupEffectManager by InternalPopupEffectManager() {

    private val container by lazy {
        MviContainer(
            initialState = XxxUiState.INITIAL_STATE,
            onIntent = { handleIntent(it) }
        )
    }

    val uiState = container.uiState
    val sideEffect = container.sideEffect

    init {
        container.intent(XxxIntent.Initialize)
    }

    fun intent(intent: XxxIntent) = container.intent(intent)

    private fun MviContext<XxxUiState, XxxSideEffect>.handleIntent(intent: XxxIntent) {
        when (intent) {
            is XxxIntent.Initialize -> {
                // TODO: Repository/UseCase 연결
            }
            // ...
        }
    }

    @AssistedFactory
    @ViewModelAssistedFactoryKey(XxxViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): XxxViewModel {
            return create(extras.createSavedStateHandle())
        }
        fun create(@Assisted savedStateHandle: SavedStateHandle): XxxViewModel
    }
}
```

- Metro DI 어노테이션 필수: `@AssistedInject`, `@AssistedFactory`, `@ViewModelAssistedFactoryKey`, `@ContributesIntoMap(AppScope::class)`
- 실제 데이터 로직(Repository/UseCase 호출)은 `// TODO` 주석으로 남긴다
- feature 모듈의 `build.gradle.kts`에 Metro 관련 의존성이 있는지 확인하고, 없으면 추가한다

### 6. Compose UI 코드 생성

Screen Composable은 **Stateless**로 작성하며, `UiState`와 콜백 람다를 파라미터로 수신한다:

```kotlin
@Composable
fun XxxScreen(
    uiState: XxxUiState,
    onAction: (XxxIntent) -> Unit,
    onBack: () -> Unit = {},
) {
    // UiState 필드에 따른 조건부 렌더링
    when {
        uiState.isLoading -> XxxLoadingContent()
        uiState.errorMessage != null -> XxxErrorContent(
            message = uiState.errorMessage,
            onRetry = { onAction(XxxIntent.Retry) }
        )
        uiState.items.isEmpty() -> XxxEmptyContent()
        else -> XxxDefaultContent(uiState = uiState, onAction = onAction)
    }
}
```

- `LinkItTheme` 내에서 구현한다
- `commonMain`에 배치하여 KMP 호환을 유지한다
- `@Preview` 함수는 `androidMain`에 **상태별로** 생성한다:
  ```kotlin
  @Preview @Composable private fun XxxScreenDefaultPreview() { ... }
  @Preview @Composable private fun XxxScreenLoadingPreview() { ... }
  @Preview @Composable private fun XxxScreenEmptyPreview() { ... }
  ```

### 7. Navigation 연결

`docs/NAVIGATION_STRUCTURE.md`의 체크리스트를 따른다:

1. **Route 추가**: `core/navigation/.../LinkItRoute.kt`의 `LinkItNavKey`에 `data object` 추가
   ```kotlin
   @Serializable
   data object Xxx : LinkItNavKey
   ```
2. **Serializer 등록**: `LinkItSavedStateConfiguration`의 `polymorphic(NavKey::class)` 블록에 추가
   ```kotlin
   subclass(LinkItNavKey.Xxx::class, LinkItNavKey.Xxx.serializer())
   ```
3. **Entry 파일 생성**: `feature/{module}/navigation/XxxEntry.kt`
   ```kotlin
   fun EntryProviderScope<NavKey>.xxxEntry(
       onBack: () -> Unit,
   ) {
       entry<LinkItNavKey.Xxx> {
           val viewModel = metroViewModel<XxxViewModel>()
           val uiState by viewModel.uiState.collectAsStateWithLifecycle()
           XxxScreen(
               uiState = uiState,
               onAction = viewModel::intent,
               onBack = onBack,
           )
       }
   }
   ```
4. **호스트 entryProvider에 등록**: `feature/home/.../HomeNavDisplay.kt`의 `entryProvider` 블록에 추가
   ```kotlin
   xxxEntry(onBack = navigator::navigateBack)
   ```
   - SITEMAP.md 계층 구조를 기반으로 **탭(top-level) vs 서브 라우트** 결정
   - 서브 라우트인 경우 해당 탭의 backstack에 push, 독립 화면이면 별도 Activity 고려

### 8. 스크린샷 테스트 작성 — 상태별

Roborazzi로 **상태별 개별 테스트 함수**를 작성한다.

```kotlin
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w375dp-h740dp-xxhdpi", application = Application::class)
class XxxScreenScreenshotTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun xxxScreen_default() {
        composeRule.setContent {
            LinkItTheme {
                XxxScreen(
                    uiState = XxxUiState(isLoading = false, items = sampleItems, errorMessage = null),
                    onAction = {},
                )
            }
        }
        composeRule.onRoot().captureRoboImage()
    }

    @Test
    fun xxxScreen_loading() {
        composeRule.setContent {
            LinkItTheme {
                XxxScreen(
                    uiState = XxxUiState(isLoading = true, items = emptyList(), errorMessage = null),
                    onAction = {},
                )
            }
        }
        composeRule.onRoot().captureRoboImage()
    }

    @Test
    fun xxxScreen_empty() { /* Empty UiState로 렌더링 */ }

    // ... 상태 수만큼 테스트 함수 생성
}
```

- 테스트 화면 크기는 Figma 프레임에서 system chrome을 제외한 콘텐츠 영역으로 설정한다
  - 예: Figma 375×812dp, status bar 38dp, home indicator 34dp → `w375dp-h740dp-xxhdpi`
- `./gradlew :모듈:recordRoborazziDebug`로 **모든 상태의 골든 이미지를 한 번에** 생성한다
- 골든 이미지는 `screenshots/` 폴더에 저장된다
- feature 모듈에서 Metro DI 관련 `MetroAppComponentFactory` 오류가 발생하면:
  - `src/androidUnitTest/AndroidManifest.xml`을 생성하여 오버라이드한다:
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools">
        <application
            android:appComponentFactory="androidx.core.app.CoreComponentFactory"
            tools:replace="android:appComponentFactory" />
    </manifest>
    ```
  - `@Config`에 `application = Application::class`를 추가한다
- 해당 모듈에 `kmp.screenshot.test.convention` 플러그인이 적용되어 있는지 확인하고, 없으면 추가한다

### 9. Figma 스크린샷 비교 — 상태별

`scripts/compare-figma.sh`로 **각 상태별로** Figma 원본과 Compose 렌더링을 픽셀 비교한다.

- 사전 조건: `FIGMA_TOKEN` 환경변수 설정 필요 (Figma Personal Access Token)
- **각 상태에 대해** 스크립트를 실행한다:
  ```
  ./scripts/compare-figma.sh <fileKey_N> <nodeId_N> <모듈> <테스트클래스> <테스트이름_N> <cropTop> <cropBottom>
  ```
  예시:
  ```
  ./scripts/compare-figma.sh abc123 1:2 feature:search SearchScreenScreenshotTest searchScreen_default 38 34
  ./scripts/compare-figma.sh abc123 3:4 feature:search SearchScreenScreenshotTest searchScreen_loading 38 34
  ./scripts/compare-figma.sh abc123 5:6 feature:search SearchScreenScreenshotTest searchScreen_empty 38 34
  ```
- 스크립트 동작 과정 (상태별 반복):
  1. Figma API로 해당 상태의 스크린샷을 다운로드한다
  2. Figma 이미지에서 status bar(상단)와 home indicator(하단)를 crop한다
  3. Compose 골든 이미지 크기에 맞게 리사이즈한다
  4. 골든 이미지를 crop된 Figma 스크린샷으로 임시 교체한다
  5. `compareRoborazziDebug`를 실행하여 diff 이미지를 생성한다
  6. 골든 이미지를 원래대로 복원한다
- **일치율 측정** (상태별): 비교 후 ImageMagick으로 픽셀 일치율을 계산한다 (fuzz 10%):
  ```bash
  GOLDEN="screenshots/...골든이미지경로.png"
  FIGMA_CROPPED="/tmp/figma_compare/figma_cropped.png"
  DIFF_PIXELS=$(magick compare -metric AE -fuzz 10% "$FIGMA_CROPPED" "$GOLDEN" null: 2>&1)
  TOTAL_PIXELS=$(magick identify -format "%[fx:w*h]" "$GOLDEN")
  MATCH=$(python3 -c "print(f'{(1 - $DIFF_PIXELS/$TOTAL_PIXELS) * 100:.1f}')")
  echo "일치율: ${MATCH}%"
  ```
- `FIGMA_TOKEN`이 없으면 `source ~/.zshrc`로 로드한다. 그래도 없으면 Material 아이콘으로 대체하고 진행한다 (사용자에게 묻지 않는다).

### 10. 반복 수정 — 상태별

각 상태가 **개별적으로** 일치율 목표에 도달할 때까지 반복 수정한다.

- **종료 조건** (다음 중 하나를 충족하면 해당 상태의 반복 종료):
  - 일치율 ≥ **90%**
  - 직전 반복 대비 일치율 개선이 **0.5% 미만** (정체)
  - 최대 **5회** 반복 도달
- **전체 종료 조건**: 모든 상태가 종료 조건을 충족하면 반복 종료
- **90% 달성 상태 처리**: 이미 90% 이상인 상태는 추가 비교에서 제외한다. 단, 코드 수정 후 회귀가 발생하지 않았는지 확인한다.
- **회귀 방지**: 한 상태의 코드를 수정한 후, 이미 통과한 다른 상태가 퇴화하지 않았는지 반드시 검증한다.
  - 코드 수정 → `recordRoborazziDebug`로 전체 골든 이미지 재생성 → 통과 상태 재비교
  - 회귀 발견 시 수정을 롤백하거나 조건부 렌더링을 조정한다
- **수정 우선순위**: diff 이미지에서 차이가 큰 영역부터 먼저 수정한다.
  1. 레이아웃/크기 차이 (높이, 너비, 위치)
  2. 색상/배경 차이
  3. 간격/여백 차이 (padding, margin, gap)
  4. 미세 차이 (1~2px, 모서리 둥글기)
- **반복 진행 로그**: 매 반복마다 상태별 일치율과 수정 내용을 기록한다:
  ```
  [반복 1]
    - Default: 82.3% → 헤더 높이 52dp 수정, 필터 칩 아이콘 교체
    - Loading: 75.1% → 스피너 위치 조정
    - Empty: 88.0% → 일러스트 크기 수정
  [반복 2]
    - Default: 91.2% ✓ 목표 도달
    - Loading: 89.5% → 스피너 색상 수정
    - Empty: 92.1% ✓ 목표 도달
  [반복 3]
    - Loading: 91.0% ✓ 목표 도달
    - 모든 상태 목표 도달, 종료
  ```
- 비교 이미지(Reference / DIFF / New 3분할)를 읽고 차이 영역을 분석한다
- 다음 기준으로 수정 가능 여부를 판단한다:
  - **수정 가능**: 여백(padding/margin), 크기, 색상, 폰트, 정렬, 간격, 모서리 둥글기 등 코드로 조정 가능한 차이
  - **수정 불가**: 플레이스홀더로 대체한 이미지/사진 영역, 플랫폼별 렌더링 차이(안티앨리어싱 등), 시스템 UI 차이, Robolectric 환경의 폰트 렌더링 차이(커스텀 폰트 미적용으로 인한 글자 폭·간격·줄바꿈 위치 차이)
- **간격/여백 또는 글자 크기 차이 발견 시 Figma 원본 값 검증**:
  diff에서 간격(padding, margin, gap, spacing)이나 글자 크기/스타일이 다르게 보이면, 감으로 수정하지 말고 반드시 Figma 원본 값을 확인한다.
  1. 1단계에서 받아둔 `get_design_context` 결과의 HTML/Tailwind 코드를 다시 참조한다.
     해당 영역의 `data-node-id`로 노드를 찾고, CSS 클래스에서 정확한 값을 읽는다.
     - 간격: `p-[17px]` → 17dp, `gap-[8px]` → 8dp, `px-[24px]` → horizontal 24dp
     - 글자 크기: `text-[16px]` → 16sp, `text-[12px]` → 12sp
     - 행간: `leading-[24px]` → lineHeight 24sp, `leading-[1.35]` → lineHeight = fontSize × 1.35
     - 자간: `tracking-[0.5px]` → letterSpacing 0.5sp
     - 굵기: `font-['...:Bold']` → FontWeight.Bold, `'...:ExtraBold'` → FontWeight.ExtraBold
  2. 확인된 Figma 값을 `docs/DESIGN_TOKEN_MAPPING.md`의 Typography 토큰과 대조한다.
     일치하는 `LinkItTextStyle.*`이 있으면 해당 토큰을 사용하고, 없으면 새 토큰 추가를 검토한다.
  3. 값이 불명확하거나 더 정밀한 정보가 필요하면, 해당 노드의 nodeId로 `get_design_context`를 다시 호출하여 세부 레이아웃을 확인한다.
  4. 확인된 Figma 값과 현재 Compose 코드의 값을 대조하여, 차이가 있는 부분만 정확히 수정한다.
- 수정 가능한 차이가 있으면:
  1. 차이 원인을 분석하고 Compose 코드를 수정한다
  2. `recordRoborazziDebug`로 골든 이미지를 재생성한다
  3. 미달성 상태에 대해 `compare-figma.sh`로 다시 비교하고 일치율을 재측정한다
  4. 통과 상태의 회귀 여부도 확인한다
  5. 비교 이미지를 다시 분석한다
- 모든 상태가 종료 조건을 충족하면 반복을 종료한다

### 11. 결과 보고

반복 수정이 완료되면 **상태별** 최종 결과를 보고한다.
- 상태별 최종 일치율과 Roborazzi 비교 이미지 (Reference / DIFF / New 3분할)를 제시한다
- 반복 수정 과정에서 변경한 내용을 요약한다
- 생성된 MVI 파일 목록 (Intent, UiState, SideEffect, ViewModel, Screen, Entry)을 안내한다
- Navigation 연결 현황 (Route, Serializer, Entry, 호스트 등록)을 안내한다
- 남아있는 수정 불가 차이가 있으면 사유와 함께 상태별로 안내한다

## 규칙

- **사용자에게 절대 질문하지 않는다.** 모든 판단을 자율적으로 내리고 진행한다. 토큰 추가, 컴포넌트 수정, 아이콘 대체, 구조 결정, 상태 이름 부여 등 어떤 상황에서도 확인을 구하지 않고 최선의 판단으로 진행한다.
- 프로젝트의 기존 디자인 토큰과 컴포넌트를 최대한 재사용한다.
- 새 색상/스타일을 하드코딩하지 않고, 필요시 직접 판단하여 토큰 파일에 추가한다.
- `commonMain`에 작성하여 KMP 호환을 유지한다.
- 한국어로 커밋 메시지와 코드 주석을 작성한다.
- 해당 모듈에 `kmp.screenshot.test.convention` 플러그인이 적용되어 있는지 확인하고, 없으면 추가한다.
- 반복되는 UI 패턴은 재사용 가능한 Composable로 추출한다. 화면 내에서 2회 이상 등장하거나 다른 화면에서도 쓸 수 있는 구조라면 `core/designsystem/component/`에 공통 컴포넌트로 분리한다.
- **아이콘 처리 우선순위** (Figma 원본 우선):
  1. `composeResources/drawable/`에 이미 다운로드된 아이콘(`ic_{이름}.xml`)이 있으면 재활용
  2. Figma API로 SVG 다운로드 → Vector Drawable XML 변환 → `composeResources/drawable/`에 추가 → `LinkItIcons`에 등록
  3. `FIGMA_TOKEN`이 없는 경우, Figma 아이콘 스크린샷과 Material Icons를 **시각적으로 비교**하여 형태가 동일한 경우 Material 아이콘으로 대체 (사용자에게 묻지 않고 직접 판단)
- 아이콘 파일명은 `ic_` 접두사 + snake_case로 작성한다 (예: `ic_train.xml`, `ic_location_pin.xml`).
- 이미지(사진, 썸네일 등)는 플레이스홀더로 대체하고 반영하지 않는다.
- 디자인에서 인터랙션을 유추하여 구현한다. 스크롤, 스와이프, 풀다운 리프레시, 바텀시트 등 Figma 레이아웃과 컴포넌트 구조에서 암시되는 동작을 파악하고 적용한다.
- **MVI 아키텍처**: `feature/home/sample/`의 기존 패턴을 정확히 따른다. Metro DI 어노테이션(`@AssistedInject`, `@AssistedFactory`, `@ViewModelAssistedFactoryKey`, `@ContributesIntoMap(AppScope::class)`)을 반드시 포함한다.
- **Navigation 연동**: `docs/NAVIGATION_STRUCTURE.md`의 "새 Route 추가 체크리스트"를 따른다. (1) `LinkItNavKey`에 route 추가, (2) `LinkItSavedStateConfiguration` serializer 등록, (3) Entry 파일 생성, (4) 호스트 `entryProvider`에 등록.
- **상태별 일치율**: 각 상태별 90% 일치율을 개별 충족해야 한다. 한 상태의 수정이 다른 상태를 퇴화시키지 않는지 확인한다.
