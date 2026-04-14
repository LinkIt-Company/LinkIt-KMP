# Figma 구현 결과 보고 (Phase 4)

이 프롬프트는 `figma-orchestrate.sh`에서 `claude -p`로 실행됩니다.

## 입력

- `.figma-workspace/analysis.md` — 분석 결과
- `.figma-workspace/implementation.md` — 구현 현황
- `.figma-workspace/verification.md` — 최종 검증 결과

## 보고 형식

아래 형식으로 **사용자에게 직접 출력**한다:

### 1. 상태별 최종 일치율

각 상태의 최종 일치율과 Roborazzi 비교 이미지 경로를 표로 제시한다.

| 상태 | 일치율 | 결과 | 비교 이미지 |
|---|---|---|---|
| Default | XX.X% | 달성/미달성 | {비교 이미지 절대경로} |
| Loading | XX.X% | 달성/미달성 | {비교 이미지 절대경로} |

### 2. 반복 수정 과정 요약

검증 루프에서 진행된 반복 횟수와 각 반복별 주요 수정 내용을 요약한다:

```
[반복 1]
  - Default: XX.X% → {수정 내용}
  - Loading: XX.X% → {수정 내용}
[반복 2]
  - Default: XX.X% → 목표 달성
  - Loading: XX.X% → {수정 내용}
...
```

### 3. 생성된 파일 목록

MVI 파일, 아이콘 파일, 테스트 파일 등 생성/수정된 모든 파일을 나열한다:
- Intent, UiState, SideEffect, ViewModel
- Screen (Content 포함)
- Entry
- 아이콘 XML 파일
- 스크린샷 테스트

### 4. Navigation 연결 현황

| 항목 | 상태 |
|---|---|
| Route 추가 (`LinkItNavKey`) | 완료/미완료 |
| Serializer 등록 | 완료/미완료 |
| Entry 파일 | 완료/미완료 |
| 호스트 entryProvider 등록 | 완료/미완료 |

### 5. 수정 불가 차이 (있을 경우)

목표 일치율에 도달하지 못한 상태가 있으면, 수정 불가 차이의 사유를 상태별로 안내한다:

| 상태 | 차이 영역 | 사유 |
|---|---|---|
| {상태명} | {영역} | {사유: 플레이스홀더 이미지, 폰트 렌더링 등} |

## 규칙

- 한국어로 출력한다.
- 비교 이미지 경로는 절대경로로 제시한다.
- 간결하고 구조화된 형식으로 작성한다.
