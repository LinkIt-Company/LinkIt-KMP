#!/bin/bash
# Figma 스크린샷을 API로 다운로드하여 Roborazzi 골든 이미지와 픽셀 비교하는 스크립트
#
# Figma 이미지에서 iOS system chrome(status bar, home indicator)을 crop하여
# Compose 렌더링과 동일한 앱 콘텐츠 영역만 비교한다.
#
# 사전 조건:
#   - FIGMA_TOKEN 환경변수 설정 (Figma Personal Access Token)
#   - Pillow 설치 (pip3 install Pillow)
#
# 사용법:
#   ./scripts/compare-figma.sh <figma_file_key> <node_id> <module> <test_class> <test_name> [crop_top_dp] [crop_bottom_dp]
#
# 예시:
#   ./scripts/compare-figma.sh ujaG7rdiEptz9whei2hw2C 15091-148462 \
#     feature:explore \
#     com.linkit.company.feature.explore.screenshot.ExploreScheduleListScreenScreenshotTest \
#     exploreScheduleListScreen_default \
#     38 34

set -e

FILE_KEY="$1"
NODE_ID="$2"
MODULE="$3"
MODULE_PATH="${3//://}"  # feature:explore -> feature/explore
TEST_CLASS="$4"
TEST_NAME="$5"
CROP_TOP_DP="${6:-0}"     # Figma status bar 높이 (dp)
CROP_BOTTOM_DP="${7:-0}"  # Figma home indicator 높이 (dp)
FIGMA_SCALE=2             # Figma API export scale

if [ -z "$FILE_KEY" ] || [ -z "$NODE_ID" ] || [ -z "$MODULE" ] || [ -z "$TEST_CLASS" ] || [ -z "$TEST_NAME" ]; then
    echo "사용법: $0 <figma_file_key> <node_id> <module> <test_class> <test_name> [crop_top_dp] [crop_bottom_dp]"
    exit 1
fi

if [ -z "$FIGMA_TOKEN" ]; then
    echo "오류: FIGMA_TOKEN 환경변수를 설정해주세요."
    echo "  export FIGMA_TOKEN=\"figd_xxxxx\""
    exit 1
fi

GOLDEN_PATH="screenshots/${MODULE_PATH}/${TEST_CLASS}.${TEST_NAME}.png"
COMPARE_PATH="screenshots/${MODULE_PATH}/${TEST_CLASS}.${TEST_NAME}_compare.png"
FIGMA_IMAGE="/tmp/figma_screenshot_$$.png"
FIGMA_CROPPED="/tmp/figma_screenshot_cropped_$$.png"

if [ ! -f "$GOLDEN_PATH" ]; then
    echo "오류: 골든 이미지를 찾을 수 없습니다: $GOLDEN_PATH"
    echo "  먼저 ./gradlew :${MODULE}:recordRoborazziDebug 를 실행해주세요."
    exit 1
fi

# 1. Figma API로 스크린샷 URL 가져오기
echo "1. Figma API에서 스크린샷 URL 가져오는 중..."
IMAGE_URL=$(curl -s -H "X-Figma-Token: $FIGMA_TOKEN" \
    "https://api.figma.com/v1/images/$FILE_KEY?ids=$NODE_ID&format=png&scale=$FIGMA_SCALE" \
    | python3 -c "import sys,json; d=json.load(sys.stdin); print(list(d['images'].values())[0])")

if [ -z "$IMAGE_URL" ] || [ "$IMAGE_URL" = "null" ]; then
    echo "오류: Figma 스크린샷 URL을 가져올 수 없습니다."
    exit 1
fi

# 2. 스크린샷 다운로드
echo "2. Figma 스크린샷 다운로드 중..."
curl -s -o "$FIGMA_IMAGE" "$IMAGE_URL"

# 3. Figma 이미지에서 system chrome crop + 골든 이미지 크기에 맞게 리사이즈
echo "3. Figma 이미지 crop + 리사이즈 중..."
if /opt/homebrew/opt/python@3.10/bin/python3.10 -c "from PIL import Image" 2>/dev/null; then
    PYTHON="/opt/homebrew/opt/python@3.10/bin/python3.10"
else
    PYTHON="python3"
fi
$PYTHON -c "
from PIL import Image

figma = Image.open('$FIGMA_IMAGE')
golden = Image.open('$GOLDEN_PATH')

crop_top = int($CROP_TOP_DP) * int($FIGMA_SCALE)
crop_bottom = int($CROP_BOTTOM_DP) * int($FIGMA_SCALE)

if crop_top > 0 or crop_bottom > 0:
    bottom = figma.height - crop_bottom
    figma = figma.crop((0, crop_top, figma.width, bottom))
    print(f'  Crop: top={crop_top}px, bottom={crop_bottom}px -> {figma.size}')

figma_resized = figma.resize(golden.size, Image.LANCZOS)
figma_resized.save('$FIGMA_CROPPED')
print(f'  Resize: {figma.size} -> {figma_resized.size} (골든 이미지 크기)')
"

# 4. 골든 이미지 백업 + Figma 스크린샷으로 교체
echo "4. Roborazzi compare 실행 중..."
cp "$GOLDEN_PATH" "${GOLDEN_PATH}.bak"
cp "$FIGMA_CROPPED" "$GOLDEN_PATH"

# 5. Compare 실행
./gradlew ":${MODULE}:cleanTestDebugUnitTest" ":${MODULE}:compareRoborazziDebug" --quiet || true

# 6. 골든 이미지 복원
mv "${GOLDEN_PATH}.bak" "$GOLDEN_PATH"

# 7. 결과 출력
if [ -f "$COMPARE_PATH" ]; then
    echo ""
    echo "비교 완료!"
    echo "  결과: $COMPARE_PATH"
    open "$COMPARE_PATH" 2>/dev/null || true
else
    echo "차이가 없거나 compare 파일이 생성되지 않았습니다."
fi

# 8. 임시 파일 정리
rm -f "$FIGMA_IMAGE" "$FIGMA_CROPPED"
