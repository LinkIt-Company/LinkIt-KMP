$ARGUMENTS 의 Figma URL들을 분석하여, 동일 화면의 여러 상태에 대한 MVI 기반 Compose UI 코드를 구현한다.

## 실행

`scripts/figma-orchestrate.sh`를 실행하여 멀티에이전트 파이프라인을 시작한다.

```bash
./scripts/figma-orchestrate.sh $ARGUMENTS
```

스크립트가 4단계 파이프라인을 자동으로 진행한다:
1. **분석** — Figma 디자인 분석, 토큰 매핑, 컴포넌트/아이콘 식별
2. **구현** — 아이콘 처리, MVI 아키텍처, Compose UI, Navigation, 스크린샷 테스트
3. **검증** — Figma 스크린샷 비교, 일치율 측정, 목표 미달 시 수정 반복 (최대 20회)
4. **보고** — 상태별 최종 일치율, 생성 파일 목록, 수정 불가 차이 안내

각 단계는 독립된 Claude 에이전트(`claude -p`)로 실행되며, 중간 결과는 `.figma-workspace/`에 저장된다.

## 사용법

```
/figma <URL1> [URL2] ... [목표일치율]
```

- URL 뒤에 숫자를 붙이면 목표 일치율로 인식 (기본값: 90%)
- 예: `/figma https://figma.com/design/xxx?node-id=1-2 85` → 목표 85%

## 환경변수

- `CLAUDE_CMD`: Claude CLI 명령어 경로 (기본값: `npx -y @anthropic-ai/claude-code`)
- `FIGMA_TOKEN`: Figma Personal Access Token (아이콘 다운로드, 스크린샷 비교에 필요)
