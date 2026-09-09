# PAX 홈페이지 — playax.kr

개인사업자 **팍스 (PAX, Play AX)** 공개 사이트입니다.
저장소: [msjycho/pax](https://github.com/msjycho/pax)

- 홈: https://playax.kr/ (DNS 연결 후)
- 방침: https://playax.kr/privacy.html
- 연결 전 임시: https://msjycho.github.io/pax/

## 가비아 DNS (지금 할 일)

네임서버는 **가비아 기본값 그대로** 두고, 레코드만 넣습니다.

| 호스트 | 타입 | 값 |
|--------|------|-----|
| `@` (또는 빈칸) | A | `185.199.108.153` |
| `@` | A | `185.199.109.153` |
| `@` | A | `185.199.110.153` |
| `@` | A | `185.199.111.153` |
| `www` | CNAME | `msjycho.github.io` |

가비아: 도메인 관리 → playax.kr → **DNS 관리** / 설정.

반영까지 수 분~수 시간 걸릴 수 있습니다. HTTPS는 GitHub가 인증서를 발급한 뒤에 됩니다.

## 그다음

1. [Search Console](https://search.google.com/search-console)에 `playax.kr` 도메인 속성 추가, TXT를 가비아 DNS에 넣고 인증
2. Play Console 조직 웹사이트에 `https://playax.kr/` 입력
3. `contact@playax.kr` 메일 포워딩 (가비아 메일)

## 아직 비어 있는 칸

대표자, 사업자등록번호, 주소, 전화. 사업자등록증을 받은 뒤 `index.html`만 고치면 됩니다.
