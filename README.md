# PAX 홈페이지 — GitHub Pages + 가비아 도메인

개인사업자 **팍스 (PAX, Play AX)** 공개 사이트입니다.
앱 저장소 `familytodoapp`은 비공개라 Pages를 켤 수 없어, 이 파일은 공개 저장소 [msjycho/pax](https://github.com/msjycho/pax)에도 올립니다.

임시 주소: `https://msjycho.github.io/pax/`
방침: `https://msjycho.github.io/pax/privacy.html`

## 가비아에서 할 일

1. 가비아에서 도메인 등록. 권장 후보: `playax.kr`, `pax.kr`, `paxplay.kr` (실제 가능 여부는 가비아에서 검색)
2. DNS 설정 (GitHub Pages 공식 A/AAAA)

루트 도메인 (`playax.kr`) A 레코드 4개:

- `185.199.108.153`
- `185.199.109.153`
- `185.199.110.153`
- `185.199.111.153`

`www` CNAME:

- 호스트 `www` → `msjycho.github.io`

3. 이 폴더의 `CNAME.example`을 `CNAME`으로 복사하고 도메인만 한 줄 적기
4. GitHub 저장소 **Settings → Pages → Custom domain**에 같은 도메인 입력 후 HTTPS 대기
5. [Search Console](https://search.google.com/search-console)에 도메인 속성 추가, TXT를 가비아 DNS에 넣고 인증
6. Play Console 조직 웹사이트 URL에 `https://도메인/` 입력 후 인증 요청

메일(`contact@도메인`)은 가비아 메일 또는 포워딩으로 만들면 됩니다.

## 아직 비어 있는 칸

홈의 주황 칸: 대표자, 사업자등록번호, 주소, 전화, 이메일.
사업자등록증을 받은 뒤 `index.html`과 `privacy.html`만 고치면 됩니다.
