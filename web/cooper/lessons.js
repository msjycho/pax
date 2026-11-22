/*
 * 쿠퍼 IT 실습실 · 레슨 데이터
 *
 * 스키마 (단원 하나 = Unit)
 * Unit {
 *   id: string                 // 주소에 쓰는 짧은 영문 id (#/unit/<id>)
 *   no: number                 // 화면에 보이는 단원 번호
 *   title: string
 *   goal: string               // 이 단원을 마치면 할 수 있는 일, 한 문장
 *   exam: string[]             // 연결되는 시험 과목 태그 (예: "정보보안기사 · 애플리케이션 보안")
 *   steps: Step[]
 * }
 * Step {
 *   id: string                 // 단원 안에서 유일
 *   title: string
 *   stream: string             // 어느 물줄기(영역)에 속하는가. COOPER_STREAMS의 id
 *   flowsFrom: string[]        // 이 개념으로 흘러드는 앞선 개념들 ("단원id/stepId" 또는 같은 단원이면 stepId)
 *   body: string[]             // 문단. `코드`와 **강조**만 지원
 *   code?: { lang: "c"|"java"|"json"|"http"|"sql", src: string }
 *   table?: { head: string[], rows: string[][] }
 *   attack?: string[]          // 공격자 관점: 이 개념이 어떻게 깨지고 어떻게 막는가
 *   checks: Check[]            // 예측형 확인 문제
 * }
 * Check {
 *   id: string                 // 단원 안에서 유일 (복습 기록의 키)
 *   q: string                  // 질문
 *   options: string[]
 *   answer: number             // options의 0부터 센 정답 위치
 *   why: string                // 정답 해설
 * }
 *
 * 세계관: 산에 떨어진 빗방울이 골짜기마다 흘러 물줄기가 되고, 다른 골짜기의 물줄기와 다시 합쳐진다.
 * 프로그래밍·자료구조·네트워크·DB·보안은 각자의 물줄기이고, flowsFrom이 합류 지점을 만든다.
 * 단원은 하나의 '합류점'(예: 장바구니 +1)을 향해 여러 물줄기가 모이는 이야기다.
 *
 * 새 단원을 추가할 때는 이 배열에 Unit을 하나 더 넣으면 된다. 화면 코드는 고칠 필요가 없다.
 */
window.COOPER_STREAMS = [
  { id: "prog", name: "프로그래밍", hint: "메모리와 객체로 값을 바꾼다" },
  { id: "ds", name: "자료구조", hint: "여러 값을 담고 찾는다" },
  { id: "net", name: "네트워크", hint: "값을 다른 컴퓨터로 보낸다" },
  { id: "data", name: "데이터베이스", hint: "값을 오래 남긴다" },
  { id: "sec", name: "보안", hint: "모든 물줄기 옆을 함께 흐른다" }
];

window.COOPER_UNITS = [
  {
    id: "cart-basics",
    no: 1,
    title: "장바구니 수량 +1, 처음부터 끝까지",
    goal: "우유 수량 2를 3으로 바꾸는 한 동작을 C 포인터부터 HTTP 요청, DB 갱신, 보안 점검까지 따라가며 설명할 수 있다.",
    exam: ["정보처리기사 · 프로그래밍 언어 활용", "정보보안기사 · 애플리케이션 보안", "정보보안기사 · 시스템 보안"],
    steps: [
      {
        id: "pointer",
        stream: "prog",
        flowsFrom: [],
        title: "C 포인터 · 주소를 따라가서 값을 바꾼다",
        code: { lang: "c", src: "int quantity = 2;\nint *p = &quantity;\n*p += 1;" },
        table: {
          head: ["표현", "의미"],
          rows: [
            ["`&quantity`", "수량 변수의 주소"],
            ["`p`", "주소를 저장한 포인터 변수"],
            ["`*p`", "포인터가 가리키는 곳의 정수"]
          ]
        },
        body: [
          "`*p += 1`을 실행하면 수량은 3이 된다. `p`에 담긴 주소는 그대로다. `p = &other_quantity`처럼 대입하면 이제 다른 변수를 가리킨다.",
          "**포인터로 대상을 찾고, `+= 1`로 값을 바꾼다.** 함수에 주소를 넘겨 호출자의 변수에 결과를 쓰거나, `scanf(\"%d\", &age)`로 입력을 저장할 때도 같은 원리다.",
          "복습할 때는 세 가지를 차례로 묻는다. **무엇을 가리키는가, 무엇이 바뀌는가, 그 대상이 아직 유효한가.**"
        ],
        attack: [
          "**버퍼 오버플로:** 포인터가 배열 끝을 넘어 쓰면 옆 메모리(반환 주소 등)를 덮어쓴다. `gets`, `strcpy` 대신 길이를 받는 `fgets`, `strncpy`를 쓰고, 컴파일러의 스택 보호와 ASLR을 켠다.",
          "**Use-after-free:** `free()` 뒤에도 같은 포인터로 접근하면 다른 데이터가 들어온 자리를 읽거나 쓴다. 해제 직후 포인터를 `NULL`로 둔다."
        ],
        checks: [
          {
            id: "pointer-1",
            q: "`*p += 1` 실행 뒤 `p`에 담긴 값은?",
            options: ["quantity의 주소 그대로", "주소가 1 증가", "3", "NULL"],
            answer: 0,
            why: "`*p`는 가리키는 대상(수량)을 바꾸고, `p` 자체의 주소 값은 바꾸지 않는다."
          },
          {
            id: "pointer-2",
            q: "`free(p)` 뒤 `*p = 5`를 실행했다. 이 결함의 이름은?",
            options: ["정수 오버플로", "Use-after-free", "SQL 삽입", "경쟁 조건"],
            answer: 1,
            why: "해제된 메모리를 다시 쓰는 것이 use-after-free다. 대상이 아직 유효한지 확인하는 세 번째 질문이 이것을 잡는다."
          }
        ]
      },
      {
        id: "struct",
        stream: "prog",
        flowsFrom: ["pointer"],
        title: "C 구조체 · 상품 한 줄을 하나로 묶는다",
        code: {
          lang: "c",
          src: "struct CartItem {\n    int product_id;\n    int quantity;\n    int unit_price;\n};\n\nstruct CartItem item = {\n    .product_id = 101,\n    .quantity = 2,\n    .unit_price = 3000\n};\n\nstruct CartItem *p = &item;\np->quantity += 1;\nitem.unit_price = 3500;"
        },
        body: [
          "`item.quantity`는 상품 변수 안의 수량이다. `p`는 구조체 전체의 주소이고 `p->quantity`는 `(*p).quantity`와 같다.",
          "`&item.quantity`는 수량 멤버 자체의 주소다. 이 주소는 `int *` 변수에 담을 수 있다.",
          "코드를 순서대로 실행하면 **수량 3개, 단가 3,500원**이다."
        ],
        checks: [
          {
            id: "struct-1",
            q: "위 코드를 끝까지 실행한 뒤 상태는?",
            options: ["수량 2, 단가 3,000", "수량 3, 단가 3,000", "수량 2, 단가 3,500", "수량 3, 단가 3,500"],
            answer: 3,
            why: "`p->quantity += 1`로 수량이 3, 이어서 `item.unit_price = 3500`으로 단가가 바뀐다."
          },
          {
            id: "struct-2",
            q: "`p->quantity`와 같은 뜻의 표현은?",
            options: ["`*p.quantity`", "`(*p).quantity`", "`&p.quantity`", "`p.quantity`"],
            answer: 1,
            why: "`.`이 `*`보다 먼저 묶이므로 괄호가 필요하다. `*p.quantity`는 `*(p.quantity)`로 해석된다."
          }
        ]
      },
      {
        id: "java-oop",
        stream: "prog",
        flowsFrom: ["struct"],
        title: "Java 객체지향 · 상품에 동작을 맡긴다",
        code: {
          lang: "java",
          src: "class CartItem {\n    private static final int MAX = 99;\n    private int quantity = 2;\n\n    void addOne() {\n        if (quantity >= MAX) throw new IllegalStateException(\"최대 수량\");\n        quantity += 1;\n    }\n\n    int getQuantity() { return quantity; }\n}\n\nCartItem item = new CartItem();\nitem.addOne();\nSystem.out.println(item.getQuantity()); // 3"
        },
        body: [
          "**클래스** `CartItem`은 데이터와 메서드를 정의하고, **객체**는 `new CartItem()`으로 만든 상품 하나다.",
          "`private`으로 수량에 대한 외부 직접 접근을 막고, `addOne()` 안에 최대 수량 같은 규칙을 둔다. 이것이 **캡슐화**다.",
          "Java는 객체를 참조로 다룬다. 참조를 대입하면 두 변수가 같은 객체를 가리키고, 메서드에 넘기면 참조 값이 복사된다."
        ],
        table: {
          head: ["비교", "C", "Java"],
          rows: [
            ["데이터에 접근", "구조체 변수 또는 포인터", "객체를 가리키는 참조"],
            ["대입", "구조체끼리는 멤버 값 복사, 포인터는 주소 복사", "참조 대입이면 같은 객체를 가리킨다"],
            ["메모리 수명", "지역 변수는 블록 끝에서 끝, `malloc`은 `free`로 해제", "도달할 수 없으면 GC가 회수"]
          ]
        },
        attack: [
          "캡슐화는 보안 경계가 아니다. 서버의 `addOne()` 규칙은 지켜지지만, 브라우저 코드는 사용자가 마음대로 바꿀 수 있다. **수량·가격 검증은 반드시 서버에서 한다.**"
        ],
        checks: [
          {
            id: "java-1",
            q: "`CartItem a = new CartItem(); CartItem b = a; b.addOne();` 뒤 `a.getQuantity()`는?",
            options: ["2", "3", "4", "컴파일 오류"],
            answer: 1,
            why: "`b = a`는 참조를 복사한다. 두 변수가 같은 객체를 가리키므로 `a`에서도 3이 보인다."
          },
          {
            id: "java-2",
            q: "`quantity`를 `private`으로 둔 가장 큰 이유는?",
            options: ["실행 속도", "메모리 절약", "규칙을 거치지 않은 수정 차단", "GC 대상 제외"],
            answer: 2,
            why: "외부에서 직접 바꾸지 못하게 하고 `addOne()`의 규칙만 거치게 하는 것이 캡슐화의 목적이다."
          }
        ]
      },
      {
        id: "list",
        stream: "ds",
        flowsFrom: ["java-oop"],
        title: "자료구조 · 상품 여러 줄을 리스트에 담는다",
        code: {
          lang: "java",
          src: "CartItem milk = new CartItem();\nCartItem bread = new CartItem();\n\nArrayList<CartItem> cart = new ArrayList<>();\ncart.add(milk);\ncart.add(bread);\ncart.get(0).addOne();"
        },
        body: [
          "`ArrayList`는 상품 객체의 **참조**를 순서대로 담는다. `get(0)`은 첫 번째 상품인 우유의 참조다.",
          "`cart.add(...)`는 **상품 줄을 추가**하고, `addOne()`은 **한 줄의 수량을 늘린다.** 둘은 다른 동작이다."
        ],
        checks: [
          {
            id: "list-1",
            q: "위 코드에 이어 `cart.get(1).addOne()`을 실행했다. 장바구니 상태는?",
            options: ["1줄, 우유 3개", "2줄, 우유 3개 · 빵 3개", "3줄", "2줄, 우유 4개 · 빵 2개"],
            answer: 1,
            why: "줄은 그대로 2줄이고, 인덱스 1인 빵의 수량이 2에서 3이 된다."
          }
        ]
      },
      {
        id: "api",
        stream: "net",
        flowsFrom: ["struct", "list"],
        title: "JSON과 API · 수량 변경을 서버에 보낸다",
        code: {
          lang: "http",
          src: "PATCH /api/cart/items/101 HTTP/1.1\nHost: playax.kr\nAuthorization: Bearer <로그인 토큰>\nContent-Type: application/json\n\n{\"delta\": 1}\n\nHTTP/1.1 200 OK\n{\"product_id\": 101, \"quantity\": 3, \"unit_price\": 3500}"
        },
        body: [
          "구조체는 프로그램 안의 **자료형**이고, JSON은 같은 데이터를 주고받거나 저장하는 **텍스트 형식**이다. 키를 멤버와 대응시킬 수 있다.",
          "`PATCH`는 자원의 일부를 고친다는 뜻이고, 경로의 `101`이 대상 상품이다. 본문에는 바꿀 양 `delta`만 보낸다.",
          "HTTPS가 이 요청을 암호화해 보호한다. DNS가 `playax.kr`의 위치를 찾고, TLS 연결 위에서 HTTP가 오간다."
        ],
        attack: [
          "**IDOR(접근통제 실패):** 경로의 id만 바꿔 다른 사람의 장바구니를 고칠 수 있다면 취약하다. 서버는 토큰의 사용자와 장바구니 소유자가 같은지 매번 확인한다. OWASP Top 10 1위 항목이다.",
          "**입력 검증:** `{\"delta\": -100}`이나 `{\"delta\": 1e9}`를 보내 보라. 서버가 범위를 검사하지 않으면 음수 수량, 정수 오버플로, 0원 주문이 생긴다.",
          "**가격 변조:** 본문에 `unit_price`를 받으면 안 된다. 가격은 서버의 상품 테이블에서 읽는다."
        ],
        checks: [
          {
            id: "api-1",
            q: "로그인한 A가 `PATCH /api/cart/items/101?cart=B의id`로 B의 장바구니를 고쳤다. 이 취약점은?",
            options: ["XSS", "CSRF", "IDOR", "SQL 삽입"],
            answer: 2,
            why: "객체 id만 바꿔 남의 자원에 접근하는 것이 IDOR(Insecure Direct Object Reference)다. 소유자 확인으로 막는다."
          },
          {
            id: "api-2",
            q: "클라이언트가 보낸 값 중 서버가 그대로 믿어도 되는 것은?",
            options: ["unit_price", "delta 범위", "user_id", "아무것도 없다"],
            answer: 3,
            why: "모든 입력은 조작될 수 있다. 사용자는 토큰에서, 가격은 DB에서 읽고, delta는 범위를 검사한다."
          }
        ]
      },
      {
        id: "db",
        stream: "data",
        flowsFrom: ["api"],
        title: "DB · 해당 상품의 수량만 저장한다",
        code: {
          lang: "sql",
          src: "UPDATE cart_items\n   SET quantity = quantity + ?\n WHERE cart_id = ?        -- 토큰의 사용자 장바구니\n   AND product_id = ?\n   AND quantity + ? BETWEEN 1 AND 99;"
        },
        body: [
          "`WHERE`가 어느 장바구니의 어느 상품인지를 고른다. `cart_id`를 빼면 모든 사람의 우유 수량이 바뀐다.",
          "`?` 자리에 값을 따로 넘기는 **바인드 변수(Prepared Statement)**를 쓴다. 문자열을 이어 붙여 쿼리를 만들지 않는다.",
          "`quantity = quantity + ?`처럼 DB 안에서 더하면 두 요청이 동시에 와도 하나가 사라지지 않는다."
        ],
        attack: [
          "**SQL 삽입:** `\"... WHERE product_id = \" + id`처럼 이어 붙이면 `101 OR 1=1`이 들어와 전체 행이 바뀐다. 바인드 변수가 기본 방어다.",
          "**경쟁 조건:** 앱에서 읽고(2) 더해서(3) 쓰는 방식은 동시 요청 두 개가 모두 3을 쓰게 만든다. 원자적 갱신이나 트랜잭션으로 막는다."
        ],
        checks: [
          {
            id: "db-1",
            q: "`WHERE product_id = 101`만 두고 `cart_id` 조건을 뺐다. 결과는?",
            options: ["오류", "내 우유만 바뀜", "모든 장바구니의 우유가 바뀜", "아무것도 안 바뀜"],
            answer: 2,
            why: "조건이 상품만 고르므로 그 상품을 담은 모든 장바구니 행이 갱신된다."
          },
          {
            id: "db-2",
            q: "SQL 삽입의 기본 방어는?",
            options: ["HTTPS 사용", "바인드 변수", "로그 남기기", "비밀번호 해시"],
            answer: 1,
            why: "값을 쿼리 문법과 분리해 넘기면 입력이 SQL로 해석되지 않는다."
          }
        ]
      },
      {
        id: "confluence",
        stream: "sec",
        flowsFrom: ["pointer", "java-oop", "api", "db"],
        title: "합류 · + 한 번에 모인 보안의 물줄기",
        body: [
          "지금까지 따로 흘러온 물줄기가 우유 옆 **+** 한 번에 모인다. 포인터가 값을 바꾸고, 객체가 규칙을 지키고, HTTP가 요청을 나르고, SQL이 행을 고른다.",
          "보안은 따로 떨어진 과목이 아니라 **모든 물줄기 옆을 함께 흐르는 물**이다. 각 층의 약한 곳이 합류점에서 하나의 사고가 된다.",
          "이 합류점은 다음 단원에서 다시 갈라진다. 주문은 트랜잭션(DB)으로, 로그인은 암호학(보안)으로, 응답 시간은 운영(네트워크)으로 흘러간다."
        ],
        table: {
          head: ["흘러온 곳", "약한 곳", "합류점에서 생기는 일", "막는 법"],
          rows: [
            ["포인터", "범위를 넘는 쓰기", "서버 프로세스 장악", "경계 검사, 스택 보호, ASLR"],
            ["객체", "브라우저 코드를 믿음", "규칙을 건너뛴 수량·가격", "서버에서 검증"],
            ["HTTP", "id만 바꾼 요청", "남의 장바구니 변경 (IDOR)", "소유자 확인"],
            ["SQL", "문자열 이어 붙이기", "전체 행 변경 (SQL 삽입)", "바인드 변수"]
          ]
        },
        checks: [
          {
            id: "confluence-1",
            q: "실습실에서 '소유자 확인'만 끄고 B의 우유 +10을 보냈다. 어느 물줄기의 약점이 드러난 것인가?",
            options: ["포인터", "자료구조", "HTTP와 접근통제", "암호화"],
            answer: 2,
            why: "요청 경로의 id를 서버가 그대로 믿었기 때문이다. HTTPS 암호화는 이 공격을 막지 못한다."
          },
          {
            id: "confluence-2",
            q: "HTTPS를 켜면 막히는 것은?",
            options: ["IDOR", "SQL 삽입", "가는 길에서 요청을 엿보거나 바꾸기", "음수 수량"],
            answer: 2,
            why: "HTTPS는 전송 구간을 보호한다. 정상 로그인한 사용자가 보내는 악의적 요청은 서버의 검증으로 막아야 한다."
          }
        ]
      }
    ]
  }
];
