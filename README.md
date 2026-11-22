# PAX 홈페이지 — playax.kr

개인사업자 **팍스 (PAX, Play AX)** 공개 사이트입니다.
저장소: [msjycho/pax](https://github.com/msjycho/pax)

- 홈: https://playax.kr/ (DNS 연결 후)
- 방침: https://playax.kr/privacy.html
- 연결 전 임시: https://msjycho.github.io/pax/

## 웹앱

브라우저에서 바로 쓰는 단일 HTML 앱. 빌드 없이 파일 하나가 곧 앱이고, 기록은 각 브라우저의 localStorage에만 저장됩니다.

- `web/routine/` — 지금 할 일 (시작 페이지): https://playax.kr/web/routine/
- `web/claude-hub/` — Claude 활용 노트: https://playax.kr/web/claude-hub/

## 도메인 연결 상태 (2026-10-25)

`playax.kr`의 DNS A 레코드가 GitHub Pages 주소로 확인되고 `https://playax.kr/`가 HTTPS 200 응답을 반환합니다. 저장소의 `CNAME` 파일에는 `playax.kr`가 설정되어 있습니다. 서브도메인을 사용하거나 DNS를 변경할 때는 도메인 관리 콘솔과 GitHub Pages 설정을 함께 확인합니다.

## 그다음

1. [Search Console](https://search.google.com/search-console)에 `playax.kr` 도메인 속성 추가, TXT를 가비아 DNS에 넣고 인증
2. Play Console 웹사이트 URL은 `https://playax.kr/` 사용
3. `contact@playax.kr` 메일 수신/포워딩 상태 확인

## 아직 비어 있는 칸

대표자, 사업자등록번호, 주소, 전화. 사업자등록증을 받은 뒤 `index.html`만 고치면 됩니다.
