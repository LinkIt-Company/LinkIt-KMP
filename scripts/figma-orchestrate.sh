#!/bin/bash
set -euo pipefail

# ============================================================================
# Figma 멀티에이전트 파이프라인 오케스트레이터
#
# 사용법:
#   ./scripts/figma-orchestrate.sh <URL1> [URL2] ... [목표일치율]
#
# 예시:
#   ./scripts/figma-orchestrate.sh https://figma.com/design/xxx?node-id=1-2
#   ./scripts/figma-orchestrate.sh https://figma.com/... https://figma.com/... 85
#
# 각 페이즈는 독립된 Claude 에이전트로 실행되며,
# 중간 결과는 .figma-workspace/ 디렉토리에 저장됩니다.
# ============================================================================

# ── 설정 ──
CLAUDE_CMD="${CLAUDE_CMD:-npx -y @anthropic-ai/claude-code}"
WORKSPACE=".figma-workspace"
COMMANDS_DIR=".claude/commands"
MAX_ITER=20

# ── 파라미터 파싱 ──
URLS=()
TARGET=90

for arg in "$@"; do
  if [[ "$arg" =~ ^[0-9]+$ ]]; then
    TARGET="$arg"
  else
    URLS+=("$arg")
  fi
done

if [ ${#URLS[@]} -eq 0 ]; then
  echo "====================================================="
  echo " Figma 멀티에이전트 파이프라인"
  echo "====================================================="
  echo ""
  echo "사용법:"
  echo "  ./scripts/figma-orchestrate.sh <URL1> [URL2] ... [목표일치율]"
  echo ""
  echo "예시:"
  echo "  ./scripts/figma-orchestrate.sh https://figma.com/design/xxx?node-id=1-2"
  echo "  ./scripts/figma-orchestrate.sh URL1 URL2 85"
  echo ""
  echo "옵션:"
  echo "  목표일치율  숫자만 입력 (기본값: 90)"
  echo ""
  echo "환경변수:"
  echo "  CLAUDE_CMD  Claude CLI 명령어 (기본값: npx -y @anthropic-ai/claude-code)"
  exit 1
fi

# ── 워크스페이스 초기화 ──
mkdir -p "$WORKSPACE"

# URL 데이터 구성
URL_DATA=""
for i in "${!URLS[@]}"; do
  URL_DATA+="- State $((i+1)): ${URLS[$i]}"$'\n'
done

echo "====================================================="
echo " Figma 멀티에이전트 파이프라인 시작"
echo "====================================================="
echo " URL 수: ${#URLS[@]}"
echo " 목표 일치율: ${TARGET}%"
echo " 최대 반복: ${MAX_ITER}회"
echo " 워크스페이스: ${WORKSPACE}/"
echo "====================================================="

# ── 공통 함수 ──
run_claude() {
  local phase_name="$1"
  local prompt="$2"

  echo ""
  echo "[${phase_name}] Claude 에이전트 실행 중..."
  $CLAUDE_CMD -p \
    --permission-mode bypassPermissions \
    "$prompt"
  echo "[${phase_name}] 에이전트 완료."
}

check_file_exists() {
  local file="$1"
  local phase="$2"
  if [ ! -f "$file" ]; then
    echo "ERROR: ${phase} 결과 파일이 생성되지 않았습니다: $file"
    echo "에이전트가 파일 작성에 실패했을 수 있습니다. 로그를 확인하세요."
    exit 1
  fi
}

# ============================================================================
# Phase 1: 분석
# ============================================================================
echo ""
echo "========================================="
echo " Phase 1: 디자인 분석"
echo "========================================="

ANALYZE_PROMPT=$(cat "${COMMANDS_DIR}/figma-analyze.md")

run_claude "분석" "$(printf '%s\n\n## 입력 URL\n\n%s' "$ANALYZE_PROMPT" "$URL_DATA")"

check_file_exists "${WORKSPACE}/analysis.md" "분석"
echo "Phase 1 완료: ${WORKSPACE}/analysis.md 생성됨"

# ============================================================================
# Phase 2: 구현 (INITIAL)
# ============================================================================
echo ""
echo "========================================="
echo " Phase 2: 구현 (INITIAL 모드)"
echo "========================================="

IMPL_PROMPT=$(cat "${COMMANDS_DIR}/figma-implement.md")

run_claude "구현" "$(printf '## 모드: INITIAL\n\n%s' "$IMPL_PROMPT")"

check_file_exists "${WORKSPACE}/implementation.md" "구현"
echo "Phase 2 완료: ${WORKSPACE}/implementation.md 생성됨"

# ============================================================================
# Phase 3: 검증 루프
# ============================================================================
echo ""
echo "========================================="
echo " Phase 3: 검증 루프 (최대 ${MAX_ITER}회)"
echo "========================================="

VERIFY_PROMPT=$(cat "${COMMANDS_DIR}/figma-verify.md")
ITERATION=0

while [ "$ITERATION" -lt "$MAX_ITER" ]; do
  ITERATION=$((ITERATION + 1))

  echo ""
  echo "-----------------------------------------"
  echo " 반복 ${ITERATION}/${MAX_ITER}"
  echo "-----------------------------------------"

  # 3a. 검증 에이전트 실행
  run_claude "검증 #${ITERATION}" \
    "$(printf '## 반복: %d\n## 목표 일치율: %d%%\n\n%s' "$ITERATION" "$TARGET" "$VERIFY_PROMPT")"

  check_file_exists "${WORKSPACE}/verification.md" "검증"

  # 3b. 종료 조건 확인
  if grep -q "ALL_PASSED: true" "${WORKSPACE}/verification.md"; then
    echo ""
    echo "=== 모든 상태 목표 달성! (반복 ${ITERATION}회) ==="
    break
  fi

  if grep -q "STALLED: true" "${WORKSPACE}/verification.md"; then
    echo ""
    echo "=== 일치율 정체 감지. 반복 종료. (반복 ${ITERATION}회) ==="
    break
  fi

  if [ "$ITERATION" -eq "$MAX_ITER" ]; then
    echo ""
    echo "=== 최대 반복 횟수(${MAX_ITER}회) 도달. 반복 종료. ==="
    break
  fi

  # 3c. 구현 에이전트 FIX 모드 실행
  echo ""
  echo "목표 미달성 상태 존재. FIX 모드로 수정 진행..."
  run_claude "수정 #${ITERATION}" \
    "$(printf '## 모드: FIX\n## 반복: %d\n\n%s' "$ITERATION" "$IMPL_PROMPT")"

done

echo ""
echo "검증 루프 완료. 총 ${ITERATION}회 반복."

# ============================================================================
# Phase 4: 결과 보고
# ============================================================================
echo ""
echo "========================================="
echo " Phase 4: 결과 보고"
echo "========================================="

REPORT_PROMPT=$(cat "${COMMANDS_DIR}/figma-report.md")

run_claude "보고" "$REPORT_PROMPT"

echo ""
echo "====================================================="
echo " 파이프라인 완료"
echo "====================================================="
echo " 검증 반복: ${ITERATION}회"
echo " 결과 파일: ${WORKSPACE}/"
echo "====================================================="
