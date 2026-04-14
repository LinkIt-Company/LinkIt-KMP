# Figma 디자인 분석 (Phase 1)

이 프롬프트는 `figma-orchestrate.sh`에서 `claude -p`로 실행됩니다.
분석 결과를 `.figma-workspace/analysis.md`에 작성하는 것이 최종 목표입니다.

## 입력

스크립트가 이 프롬프트 앞에 Figma URL 목록을 삽입합니다.

## 진행 순서

### 1. URL 파싱

- 입력된 Figma URL들에서 `fileKey`와 `nodeId`를 추출한다 (nodeId의 `-`는 `:`로 변환).
- URL이 1개인 경우에도 동일한 워크플로우로 진행한다 (상태가 1개인 화면으로 취급).
- 임시 인덱스(State 1, State 2, ...)를 부여하고, 2단계에서 상태 이름을 확정한다.

### 2. Figma 디자인 컨텍스트 수집

`figma:figma-implement-design` 스킬을 사용하여 디자인 컨텍스트를 가져온다.

1. **모든 URL**에 대해 `get_design_context`를 호출하여 디자인 정보와 스크린샷을 가져온다.
   - 여러 URL이 있으면 병렬 Agent로 동시 수집이 가능하다.
2. `docs/SITEMAP.md`를 읽고, Figma 디자인과 대조하여 **해당 화면이 어떤 스크린인지** 식별한다.
3. 각 URL의 시각적 차이를 분석하여 **상태 이름을 자동 부여**한다:
   - 콘텐츠 유무 → Default vs Empty
   - 로딩 인디케이터 존재 → Loading
   - 에러 메시지/재시도 버튼 → Error
   - 인터랙션 차이 → Selected, Expanded, Filtered 등
   - 상태명은 영문 PascalCase로 작성한다 (예: `Default`, `Loading`, `Empty`, `Error`)

### 3. 디자인 토큰 매핑

`docs/DESIGN_TOKEN_MAPPING.md`를 읽고, Figma 변수를 프로젝트 토큰에 매핑한다.
- `Color.kt`, `Type.kt`, `Shape.kt`의 기존 토큰을 우선 사용한다.
- 매핑되지 않는 새 토큰이 있으면 식별하여 기록한다.
- 모든 상태에 공통으로 적용한다.

### 4. 기존 컴포넌트 인벤토리

`core/designsystem/src/commonMain/kotlin/.../component/` 디렉토리의 기존 컴포넌트를 스캔한다.
- LinkItButton, LinkItTag, LinkItTextField, LinkItTopAppBar, LinkItFilterChip 등
- 디자인에서 재사용 가능한 컴포넌트를 식별한다.
- 새로 필요한 컴포넌트가 있으면 기록한다.

### 5. 아이콘 식별

**모든 상태**의 디자인에서 아이콘을 수집하고, 중복을 제거한 뒤 목록화한다.

- `get_design_context` 결과에서 아이콘 에셋을 식별한다 (data-name 속성에 아이콘 이름이 표시됨).
- **아이콘 식별 기준**: `<img>` 태그로 렌더링되고, 크기가 작은(24dp 이하) 요소는 아이콘으로 판단한다.
- 기존 아이콘 확인: `core/designsystem/src/commonMain/composeResources/drawable/`에 동일 이름(`ic_{아이콘명}.xml`)의 파일이 이미 있는지 확인한다.
- 각 아이콘의 `nodeId`(data-node-id 속성)를 수집한다.
- 이미지(사진, 썸네일 등)는 아이콘이 아니므로 별도 표기한다.

### 6. Figma 프레임 정보 수집

- Figma 프레임의 전체 크기(W x H dp)를 확인한다.
- Status bar 높이와 home indicator 높이를 측정한다.
- 이 정보는 검증 단계에서 crop 파라미터로 사용된다.

## 출력

`.figma-workspace/analysis.md`에 아래 형식으로 작성한다:

```markdown
# 분석 결과

## 화면 정보
- screenName: {화면 이름, PascalCase}
- module: feature:{모듈명}
- 프레임: {W}x{H} dp
- statusBar: {N}dp
- homeIndicator: {N}dp

## 상태 목록

### State: {상태명}
- fileKey: {fileKey}
- nodeId: {nodeId}
- designContext 요약:
  - 레이아웃: {핵심 레이아웃 구조}
  - 주요 색상: {사용된 색상 토큰}
  - 타이포그래피: {사용된 텍스트 스타일}
  - 핵심 요소: {주요 UI 요소 설명}

(상태 수만큼 반복)

## 토큰 매핑

| Figma 변수 | Compose 토큰 | 신규 여부 |
|---|---|---|
| {변수명} | {토큰명} | 기존/신규 |

## 컴포넌트 매핑

| 디자인 요소 | 기존 컴포넌트 | 비고 |
|---|---|---|
| {요소 설명} | {컴포넌트명 또는 "신규 필요"} | {참고사항} |

## 아이콘 목록

| 아이콘 이름 | nodeId | 기존 파일 | 처리 방법 |
|---|---|---|---|
| {이름} | {nodeId} | 있음/없음 | 재활용/다운로드필요 |

## designContext 원본 데이터

(각 상태별 get_design_context의 HTML/Tailwind 원본을 그대로 포함.
 구현 단계에서 간격/크기 값을 참조할 때 사용.)

### {상태명} designContext
{get_design_context 원본}
```

## 규칙

- **사용자에게 절대 질문하지 않는다.** 모든 판단을 자율적으로 내리고 진행한다.
- 한국어로 분석 결과를 작성한다.
- designContext 원본 데이터는 반드시 포함한다 (구현 단계에서 정확한 값 참조에 필수).
- `figma:figma-implement-design` 스킬을 사용하여 `get_design_context`를 호출한다.
