# Figma 디자인 검증 (Phase 3)

이 프롬프트는 `figma-orchestrate.sh`에서 `claude -p`로 실행됩니다.
스크립트가 프롬프트 앞에 `## 반복: N`과 `## 목표 일치율: N%`를 삽입합니다.

## 입력

- `.figma-workspace/analysis.md` — 분석 결과 (상태별 fileKey, nodeId, 프레임 정보)
- `.figma-workspace/implementation.md` — 구현 현황 (모듈, 테스트 클래스, 테스트 이름)
- `.figma-workspace/verification.md` — 이전 검증 결과 (있을 경우)

## 진행 순서

### 1. 입력 데이터 읽기

`.figma-workspace/analysis.md`와 `.figma-workspace/implementation.md`를 읽어서:
- 상태별 fileKey, nodeId
- 모듈 경로 (예: `feature:map`)
- 테스트 클래스명
- 상태별 테스트 함수명
- Figma 프레임의 statusBar, homeIndicator dp 값

이전 `verification.md`가 있으면 읽어서:
- 이미 목표 달성한 상태 목록
- 이전 반복의 일치율

### 2. 골든 이미지 생성

`./gradlew :{모듈}:recordRoborazziDebug`를 실행하여 최신 골든 이미지를 생성/갱신한다.

### 3. 상태별 Figma 비교

각 상태에 대해 `scripts/compare-figma.sh`를 실행한다:

```bash
./scripts/compare-figma.sh <fileKey> <nodeId> <모듈> <테스트클래스> <테스트이름> <cropTop> <cropBottom>
```

예시:
```bash
./scripts/compare-figma.sh abc123 1:2 feature:search SearchScreenScreenshotTest searchScreen_default 38 34
./scripts/compare-figma.sh abc123 3:4 feature:search SearchScreenScreenshotTest searchScreen_loading 38 34
```

- 이미 목표 달성한 상태도 **회귀 체크를 위해** 비교를 실행한다.

### 4. 일치율 측정

비교 후 ImageMagick으로 픽셀 일치율을 계산한다 (fuzz 10%):

```bash
GOLDEN="screenshots/...골든이미지경로.png"
FIGMA_CROPPED="/tmp/figma_compare/figma_cropped.png"
DIFF_PIXELS=$(magick compare -metric AE -fuzz 10% "$FIGMA_CROPPED" "$GOLDEN" null: 2>&1)
TOTAL_PIXELS=$(magick identify -format "%[fx:w*h]" "$GOLDEN")
MATCH=$(python3 -c "print(f'{(1 - $DIFF_PIXELS/$TOTAL_PIXELS) * 100:.1f}')")
echo "일치율: ${MATCH}%"
```

### 5. Diff 분석

비교 이미지 (Reference / DIFF / New 3분할)를 읽고 차이 영역을 분석한다.

다음 기준으로 수정 가능 여부를 판단한다:
- **수정 가능**: 여백(padding/margin), 크기, 색상, 폰트, 정렬, 간격, 모서리 둥글기 등 코드로 조정 가능한 차이
- **수정 불가**: 플레이스홀더로 대체한 이미지/사진 영역, 플랫폼별 렌더링 차이(안티앨리어싱 등), 시스템 UI 차이, Robolectric 환경의 폰트 렌더링 차이(커스텀 폰트 미적용으로 인한 글자 폭/간격/줄바꿈 위치 차이)

수정 가능한 차이에 대해 **구체적 수정 지침**을 작성한다:
- 어떤 Composable의 어떤 속성을 어떤 값으로 변경해야 하는지
- 가능하면 파일 경로와 대략적인 위치를 명시

### 6. 회귀 체크

이전 반복에서 이미 목표를 달성한 상태가 퇴화하지 않았는지 확인한다.
- 이전 일치율보다 2% 이상 감소하면 회귀로 판정
- 회귀 발생 시 수정 지침에 "회귀 발생" 경고를 포함

### 7. 종료 조건 판정

모든 상태의 결과를 종합하여 종료 조건을 판정한다:
- **ALL_PASSED**: 모든 상태가 목표 일치율 이상
- **STALLED**: 모든 미달성 상태의 일치율 개선이 직전 대비 0.5% 미만

## 출력

`.figma-workspace/verification.md`에 아래 형식으로 작성한다:

```markdown
# 검증 결과 (반복 N회)

## 상태별 결과

### {상태명}
- 일치율: XX.X%
- 이전 일치율: XX.X% (첫 반복이면 "없음")
- 개선: +X.X% (또는 "첫 반복")
- 상태: 달성/미달성/정체/회귀
- 수정 가능 차이:
  1. {영역}: {현재값} → {Figma값}. {수정 방법}
  2. ...
- 수정 불가 차이:
  1. {영역}: {사유}
  2. ...

(상태 수만큼 반복)

## 전체 요약
- 달성: N/M 상태
- 미달성: {상태명 목록}
- ALL_PASSED: true/false
- STALLED: true/false
```

**중요**: `ALL_PASSED`와 `STALLED` 플래그는 반드시 `## 전체 요약` 섹션에 정확히 `ALL_PASSED: true` 또는 `ALL_PASSED: false` 형식으로 작성한다. 오케스트레이터 스크립트가 grep으로 이 값을 파싱한다.

## 규칙

- **사용자에게 절대 질문하지 않는다.**
- `FIGMA_TOKEN`이 없으면 `source ~/.zshrc`로 로드한다. 그래도 없으면 비교를 스킵하고 수동 확인이 필요함을 기록한다.
- 한국어로 결과를 작성한다.
- 비교 이미지 경로는 절대경로로 기록한다.
