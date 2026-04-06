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

5. **스크린샷 테스트 작성**: Roborazzi로 시각적 검증을 수행한다.
   - 생성된 화면의 스크린샷 테스트를 `androidUnitTest`에 작성한다
   - `./gradlew :모듈:recordRoborazziDebug`로 골든 이미지를 생성한다
   - Figma 스크린샷과 비교하여 구현 정확도를 시각적으로 확인한다

6. **결과 확인**: 구현 결과를 사용자에게 보여주고 피드백을 요청한다.
   - Figma 원본 스크린샷과 Compose 스크린샷을 함께 제시한다
   - 수정이 필요한 부분을 반영한다

## 규칙

- 프로젝트의 기존 디자인 토큰과 컴포넌트를 최대한 재사용한다.
- 새 색상/스타일을 하드코딩하지 않고, 필요시 토큰 파일에 추가한다.
- `commonMain`에 작성하여 KMP 호환을 유지한다.
- 한국어로 커밋 메시지와 코드 주석을 작성한다.
- 해당 모듈에 `kmp.screenshot.test.convention` 플러그인이 적용되어 있는지 확인하고, 없으면 추가한다.
