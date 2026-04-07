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

4. **Compose UI 코드 생성**: 디자인에 맞는 Compose 코드를 작성한다.
   - `LinkItTheme` 내에서 구현한다
   - `commonMain`에 배치하여 KMP 호환을 유지한다
   - `@Preview` 함수도 `androidMain`에 함께 생성한다

5. **스크린샷 테스트 작성**: Roborazzi로 Compose 렌더링 골든 이미지를 생성한다.
   - 생성된 화면의 스크린샷 테스트를 `androidUnitTest`에 작성한다
   - `createComposeRule()` + `onRoot().captureRoboImage()`로 Composable만 캡처한다
   - 테스트 화면 크기는 Figma 프레임에서 system chrome을 제외한 콘텐츠 영역으로 설정한다
     - 예: Figma 375×812dp, status bar 38dp, home indicator 34dp → `w375dp-h740dp-xxhdpi`
   - `./gradlew :모듈:recordRoborazziDebug`로 골든 이미지를 생성한다
   - 골든 이미지는 `screenshots/` 폴더에 저장된다

6. **Figma 스크린샷 비교**: `scripts/compare-figma.sh`로 Figma 원본과 Compose 렌더링을 픽셀 비교한다.
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

7. **반복 수정**: 비교 이미지를 분석하고, 수정 가능한 차이가 없을 때까지 구현을 반복 수정한다.
   - 비교 이미지(Reference / DIFF / New 3분할)를 읽고 차이 영역을 분석한다
   - 다음 기준으로 수정 가능 여부를 판단한다:
     - **수정 가능**: 여백(padding/margin), 크기, 색상, 폰트, 정렬, 간격, 모서리 둥글기 등 코드로 조정 가능한 차이
     - **수정 불가**: 플레이스홀더로 대체한 이미지/사진 영역, 플랫폼별 렌더링 차이(안티앨리어싱 등), 시스템 UI 차이
   - 수정 가능한 차이가 있으면:
     1. 차이 원인을 분석하고 Compose 코드를 수정한다
     2. `recordRoborazziDebug`로 골든 이미지를 재생성한다
     3. `compare-figma.sh`로 다시 비교한다
     4. 비교 이미지를 다시 분석한다
   - 수정 가능한 차이가 없을 때 반복을 종료한다

8. **결과 보고**: 반복 수정이 완료되면 최종 결과를 보고한다.
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
- 아이콘은 Figma에서 에셋 URL을 다운로드하여 프로젝트 리소스에 추가하거나, `LinkItIcons`에 Material 아이콘을 등록하여 사용한다. 이미지(사진, 썸네일 등)는 플레이스홀더로 대체하고 반영하지 않는다.
- 디자인에서 인터랙션을 유추하여 구현한다. 스크롤, 스와이프, 풀다운 리프레시, 바텀시트 등 Figma 레이아웃과 컴포넌트 구조에서 암시되는 동작을 파악하고 적용한다.
