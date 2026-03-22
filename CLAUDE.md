# CLAUDE.md

## 브랜치 네이밍 규칙

형식: `{작업종류}/#{이슈번호}-{작업내용}`

| 작업 종류　 | prefix     |
| -------------| ------------|
| New Feature | `feature`  |
| Improvement | `improve`  |
| Bug Fix　　 | `fix`      |
| Refactoring | `refactor` |
| Chore　　　 | `chore`    |

- 작업 내용은 영문 소문자 snake_case로 간결하게 작성
- `develop` 브랜치에서 분기
- 예시: `feature/#15-home_screen`, `fix/#20-login_crash`

## 브랜치 계층 구조

```
main          ← 최상위 (릴리스)
└── develop   ← 개발 통합 브랜치
    └── feature/*, fix/*, improve/*, refactor/*, chore/*  ← 작업 브랜치
```

- 작업 브랜치 → `develop` 으로 머지
- `develop` → `main` 으로 머지

## 커밋 메시지 규칙

형식: `{타입}: 간결한 설명`

| 타입　　　 | 설명　　　　　　　　　　　　 |
| ------------| ------------------------------|
| `feat`　　 | 새로운 기능　　　　　　　　　|
| `fix`　　　| 버그 수정　　　　　　　　　　|
| `refactor` | 리팩토링　　　　　　　　　　 |
| `chore`　　| 빌드, 설정 등 기타　　　　　 |
| `docs`　　 | 문서 변경　　　　　　　　　　|
| `style`　　| 코드 포맷팅 (동작 변경 없음) |
| `test`　　 | 테스트 추가/수정　　　　　　 |

- 예시: `feat: 홈 화면 UI 구현`, `fix: 로그인 시 크래시 수정`
