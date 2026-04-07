#!/bin/bash
# Figma 스크린샷을 API로 다운로드하여 Roborazzi 골든 이미지와 픽셀 비교하는 스크립트
#
# 사전 조건:
#   - FIGMA_TOKEN 환경변수 설정 (Figma Personal Access Token)
#   - Pillow 설치 (pip3 install Pillow)
#
# 사용법:
#   ./scripts/compare-figma.sh <figma_file_key> <node_id> <module> <test_class> <test_name>
#
# 예시:
#   ./scripts/compare-figma.sh ujaG7rdiEptz9whei2hw2C 15091-148462 \
#     feature:schedule \
#     com.linkit.company.feature.schedule.screenshot.ScheduleListScreenScreenshotTest \
#     scheduleListScreen_default

set -e

FILE_KEY="$1"
NODE_ID="$2"
MODULE="$3"
MODULE_PATH="${3//://}"  # feature:schedule -> feature/schedule
TEST_CLASS="$4"
TEST_NAME="$5"

if [ -z "$FILE_KEY" ] || [ -z "$NODE_ID" ] || [ -z "$MODULE" ] || [ -z "$TEST_CLASS" ] || [ -z "$TEST_NAME" ]; then
    echo "사용법: $0 <figma_file_key> <node_id> <module> <test_class> <test_name>"
    exit 1
fi

if [ -z "$FIGMA_TOKEN" ]; then
    echo "오류: FIGMA_TOKEN 환경변수를 설정해주세요."
    echo "  export FIGMA_TOKEN=\"figd_xxxxx\""
    exit 1
fi

GOLDEN_PATH="$MODULE_PATH/build/outputs/roborazzi/${TEST_CLASS}.${TEST_NAME}.png"
COMPARE_PATH="$MODULE_PATH/build/outputs/roborazzi/${TEST_CLASS}.${TEST_NAME}_compare.png"
FIGMA_IMAGE="/tmp/figma_screenshot_$$.png"
FIGMA_RESIZED="/tmp/figma_screenshot_resized_$$.png"

# 1. Figma API로 스크린샷 URL 가져오기
echo "1. Figma API에서 스크린샷 URL 가져오는 중..."
IMAGE_URL=$(curl -s -H "X-Figma-Token: $FIGMA_TOKEN" \
    "https://api.figma.com/v1/images/$FILE_KEY?ids=$NODE_ID&format=png&scale=2" \
    | python3 -c "import sys,json; d=json.load(sys.stdin); print(list(d['images'].values())[0])")

if [ -z "$IMAGE_URL" ] || [ "$IMAGE_URL" = "null" ]; then
    echo "오류: Figma 스크린샷 URL을 가져올 수 없습니다."
    exit 1
fi

# 2. 스크린샷 다운로드
echo "2. Figma 스크린샷 다운로드 중..."
curl -s -o "$FIGMA_IMAGE" "$IMAGE_URL"

# 3. 골든 이미지 크기에 맞게 리사이즈
echo "3. 골든 이미지 크기에 맞게 리사이즈 중..."
PYTHON=$(command -v python3)
$PYTHON -c "
from PIL import Image
golden = Image.open('$GOLDEN_PATH')
figma = Image.open('$FIGMA_IMAGE')
figma_resized = figma.resize(golden.size, Image.LANCZOS)
figma_resized.save('$FIGMA_RESIZED')
print(f'  Figma {figma.size} -> {figma_resized.size} (골든 이미지 크기)')
" 2>/dev/null || {
    # Pillow 없으면 python3.10으로 시도
    PYTHON=$(command -v python3.10 || echo "/opt/homebrew/opt/python@3.10/bin/python3.10")
    $PYTHON -c "
from PIL import Image
golden = Image.open('$GOLDEN_PATH')
figma = Image.open('$FIGMA_IMAGE')
figma_resized = figma.resize(golden.size, Image.LANCZOS)
figma_resized.save('$FIGMA_RESIZED')
print(f'  Figma {figma.size} -> {figma_resized.size} (골든 이미지 크기)')
"
}

# 4. 골든 이미지 백업 + Figma 스크린샷으로 교체
echo "4. Roborazzi compare 실행 중..."
cp "$GOLDEN_PATH" "${GOLDEN_PATH}.bak"
cp "$FIGMA_RESIZED" "$GOLDEN_PATH"

# 5. Compare 실행
./gradlew ":${MODULE}:cleanTestDebugUnitTest" ":${MODULE}:compareRoborazziDebug" --quiet || true

# 6. 골든 이미지 복원
mv "${GOLDEN_PATH}.bak" "$GOLDEN_PATH"

# 7. 결과를 screenshots/compare/ 에 저장
OUTPUT_DIR="screenshots/compare"
mkdir -p "$OUTPUT_DIR"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
SAVED_PATH="$OUTPUT_DIR/${TEST_NAME}_${TIMESTAMP}_compare.png"

if [ -f "$COMPARE_PATH" ]; then
    cp "$COMPARE_PATH" "$SAVED_PATH"
    echo ""
    echo "비교 완료!"
    echo "  결과: $SAVED_PATH"
    open "$SAVED_PATH"
else
    echo "차이가 없거나 compare 파일이 생성되지 않았습니다."
fi

# 8. 임시 파일 정리
rm -f "$FIGMA_IMAGE" "$FIGMA_RESIZED"
