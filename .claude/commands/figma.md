$ARGUMENTS URL의 Figma 디자인을 Compose UI 코드로 구현한다.

## 진행 순서

1. **Figma 디자인 분석**: `figma:figma-implement-design` 스킬을 사용하여 디자인 컨텍스트를 가져온다.
   - URL에서 fileKey와 nodeId를 추출한다
   - `get_design_context`로 디자인 정보와 스크린샷을 가져온다

2. **디자인 토큰 매핑**: `docs/DESIGN_TOKEN_MAPPING.md`를 읽고, Figma 변수를 프로젝트 토큰에 매핑한다.
   - `Color.kt`, `Type.kt`, `Shape.kt`의 기존 토큰을 우선 사용한다
   - 매핑되지 않는 새 토큰이 있으면 사용자에게 확인 후 토큰 파일에 추가한다

3. **기존 컴포넌트 활용**: `core/designsystem/src/commonMain/kotlin/.../component/` 디렉토리의 기존 컴포넌트를 최대한 활용하여 구현한다.
   - LinkItButton, LinkItTag, LinkItTextField, LinkItTopAppBar, LinkItFilterChip 등
   - 새 컴포넌트가 필요한 경우 기존 컴포넌트 패턴을 따라 작성한다

4. **아이콘 처리**: 디자인에 사용된 아이콘을 Figma에서 가져와 프로젝트에 추가한다.
   - `get_design_context` 결과에서 아이콘 에셋을 식별한다 (data-name 속성에 아이콘 이름이 표시됨)
   - **아이콘 식별 기준**: `<img>` 태그로 렌더링되고, 크기가 작은(24dp 이하) 요소는 아이콘으로 판단한다
   - **`FIGMA_TOKEN` 사전 확인**: 아이콘이 1개라도 식별되면 즉시 `echo $FIGMA_TOKEN`으로 토큰 존재 여부를 확인한다. 없으면 사용자에게 요청하고, 토큰을 받을 때까지 아이콘 처리를 대기한다.
   - **아이콘 처리 우선순위** (Figma 원본 다운로드를 기본으로 한다):
     1. **기존 다운로드 확인**: `core/designsystem/src/commonMain/composeResources/drawable/`에 동일 이름(`ic_{아이콘명}.xml`)의 파일이 이미 있으면 그대로 재활용한다.
     2. **Figma에서 SVG 다운로드** (기본 방식): 기존 파일이 없으면 아래 절차로 Figma에서 직접 가져온다.
     3. **Material 아이콘 대체** (최후 수단): `FIGMA_TOKEN`을 받을 수 없는 경우에만, `get_screenshot`으로 Figma 아이콘 스크린샷을 찍고 Material Icons와 **시각적으로 비교**하여 형태가 동일한 경우에 한해 대체한다. 이름만 같고 형태가 다르면 대체하지 않는다.
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

5. **Compose UI 코드 생성**: 디자인에 맞는 Compose 코드를 작성한다.
   - `LinkItTheme` 내에서 구현한다
   - `commonMain`에 배치하여 KMP 호환을 유지한다
   - `@Preview` 함수도 `androidMain`에 함께 생성한다

6. **스크린샷 테스트 작성**: Roborazzi로 Compose 렌더링 골든 이미지를 생성한다.
   - 생성된 화면의 스크린샷 테스트를 `androidUnitTest`에 작성한다
   - `createComposeRule()` + `onRoot().captureRoboImage()`로 Composable만 캡처한다
   - 테스트 화면 크기는 Figma 프레임에서 system chrome을 제외한 콘텐츠 영역으로 설정한다
     - 예: Figma 375×812dp, status bar 38dp, home indicator 34dp → `w375dp-h740dp-xxhdpi`
   - `./gradlew :모듈:recordRoborazziDebug`로 골든 이미지를 생성한다
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

7. **Figma 스크린샷 비교 및 일치율 측정**: `scripts/compare-figma.sh`로 Figma 원본과 Compose 렌더링을 픽셀 비교한다.
   - 사전 조건: `FIGMA_TOKEN` 환경변수 설정 필요 (Figma Personal Access Token)
   - URL에서 추출한 fileKey와 nodeId를 사용한다
   - Figma의 status bar / home indicator 높이(dp)를 crop 파라미터로 전달한다
   - 스크립트 실행:
     ```
     ./scripts/compare-figma.sh <fileKey> <nodeId> <모듈> <테스트클래스> <테스트이름> <cropTop> <cropBottom>
     ```
   - 스크립트 동작 과정:
     1. Figma API로 스크린샷을 다운로드한다
     2. Figma 이미지에서 status bar(상단)와 home indicator(하단)를 crop한다
     3. Compose 골든 이미지 크기에 맞게 리사이즈한다
     4. 골든 이미지를 crop된 Figma 스크린샷으로 임시 교체한다
     5. `compareRoborazziDebug`를 실행하여 diff 이미지를 생성한다
     6. 골든 이미지를 원래대로 복원한다
   - 비교 결과는 `screenshots/` 폴더에 `*_compare.png`로 저장된다
   - 비교 이미지에서 빨간 부분이 Figma와 Compose의 차이 영역이다
   - `FIGMA_TOKEN`이 없으면 사용자에게 요청한다
   - **일치율 측정**: 비교 후 ImageMagick으로 픽셀 일치율을 계산한다 (fuzz 10%로 폰트/안티앨리어싱 차이 허용):
     ```bash
     GOLDEN="screenshots/...골든이미지경로.png"
     FIGMA_CROPPED="/tmp/figma_compare/figma_cropped.png"
     DIFF_PIXELS=$(magick compare -metric AE -fuzz 10% "$FIGMA_CROPPED" "$GOLDEN" null: 2>&1)
     TOTAL_PIXELS=$(magick identify -format "%[fx:w*h]" "$GOLDEN")
     MATCH=$(python3 -c "print(f'{(1 - $DIFF_PIXELS/$TOTAL_PIXELS) * 100:.1f}')")
     echo "일치율: ${MATCH}%"
     ```

8. **반복 수정**: 일치율 목표에 도달할 때까지 구현을 반복 수정한다.
   - **종료 조건** (다음 중 하나를 충족하면 반복 종료):
     - 일치율 ≥ **90%**
     - 직전 반복 대비 일치율 개선이 **0.5% 미만** (정체)
     - 최대 **5회** 반복 도달
   - **수정 우선순위**: diff 이미지에서 차이가 큰 영역부터 먼저 수정한다.
     1. 레이아웃/크기 차이 (높이, 너비, 위치)
     2. 색상/배경 차이
     3. 간격/여백 차이 (padding, margin, gap)
     4. 미세 차이 (1~2px, 모서리 둥글기)
   - **반복 진행 로그**: 매 반복마다 일치율과 수정 내용을 기록한다:
     ```
     [반복 1] 일치율 72.3% → 헤더 높이 52dp 수정, 필터 칩 아이콘 교체
     [반복 2] 일치율 85.1% → 카드 간격 6dp→8dp, 메타 텍스트 색상 수정
     [반복 3] 일치율 90.2% → 목표 도달, 종료
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
     3. `compare-figma.sh`로 다시 비교하고 일치율을 재측정한다
     4. 비교 이미지를 다시 분석한다
   - 일치율 목표 도달 또는 종료 조건 충족 시 반복을 종료한다

9. **결과 보고**: 반복 수정이 완료되면 최종 결과를 보고한다.
   - 최종 Roborazzi 비교 이미지 (Reference / DIFF / New 3분할)를 제시한다
   - 반복 수정 과정에서 변경한 내용을 요약한다
   - 남아있는 수정 불가 차이가 있으면 사유와 함께 안내한다

## 규칙

- 프로젝트의 기존 디자인 토큰과 컴포넌트를 최대한 재사용한다.
- 새 색상/스타일을 하드코딩하지 않고, 필요시 토큰 파일에 추가한다.
- `commonMain`에 작성하여 KMP 호환을 유지한다.
- 한국어로 커밋 메시지와 코드 주석을 작성한다.
- 해당 모듈에 `kmp.screenshot.test.convention` 플러그인이 적용되어 있는지 확인하고, 없으면 추가한다.
- 반복되는 UI 패턴은 재사용 가능한 Composable로 추출한다. 화면 내에서 2회 이상 등장하거나 다른 화면에서도 쓸 수 있는 구조라면 `core/designsystem/component/`에 공통 컴포넌트로 분리한다.
- **아이콘 처리 우선순위** (Figma 원본 우선):
  1. `composeResources/drawable/`에 이미 다운로드된 아이콘(`ic_{이름}.xml`)이 있으면 재활용
  2. Figma API로 SVG 다운로드 → Vector Drawable XML 변환 → `composeResources/drawable/`에 추가 → `LinkItIcons`에 등록
  3. `FIGMA_TOKEN` 없이 진행할 수밖에 없는 경우에만, Figma 아이콘 스크린샷과 Material Icons를 **시각적으로 비교**하여 형태가 동일한 경우 Material 아이콘으로 대체
- 아이콘 파일명은 `ic_` 접두사 + snake_case로 작성한다 (예: `ic_train.xml`, `ic_location_pin.xml`).
- 이미지(사진, 썸네일 등)는 플레이스홀더로 대체하고 반영하지 않는다.
- 디자인에서 인터랙션을 유추하여 구현한다. 스크롤, 스와이프, 풀다운 리프레시, 바텀시트 등 Figma 레이아웃과 컴포넌트 구조에서 암시되는 동작을 파악하고 적용한다.
