현재 브랜치의 변경사항을 기반으로 GitHub Pull Request를 생성해줘.

## 절차

1. `git status`로 커밋되지 않은 변경사항이 있는지 확인해. 커밋되지 않은 변경이 있으면 사용자에게 먼저 커밋하라고 안내하고 중단해.
2. `git log main..HEAD --oneline`으로 현재 브랜치의 커밋 목록을 확인해.
3. `git diff main...HEAD`로 전체 변경 내용을 파악해.
4. 브랜치 이름에서 이슈 번호를 추출해 (예: `feature/#4-navigation` → `#4`).
5. 변경 내용을 분석하여 아래 PR 템플릿에 맞게 내용을 채워.
6. 리모트에 푸시되지 않았다면 `git push -u origin <branch>` 로 푸시해.
7. `gh pr create`로 PR을 생성해.

## PR 템플릿

```
### What is this PR (Required)
- **Issue Number** : close #<이슈번호>
- 기타 관련 문서 :

### Changes (Required)
- <변경사항을 구체적으로 bullet point로 작성>

### Review Point (Required)
- <리뷰어가 주의 깊게 봐야 할 포인트를 작성>
```

## 규칙

- PR 제목은 한국어로 작성하고, 70자 이내로 간결하게 작성해.
- PR 제목 형식: `[#이슈번호] 변경 내용 요약` (예: `[#4] 바텀 네비게이션 도입 및 화면 구조 재설계`)
- Changes 섹션은 커밋 메시지와 diff를 분석해서 구체적으로 작성해.
- Review Point는 구조적 변경, 새로운 패턴 도입 등 리뷰어가 집중해야 할 부분을 작성해.
- Screenshot 섹션은 생략해.
- 마지막에 PR URL을 출력해줘.
