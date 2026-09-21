# PlayAX Novel (Android scaffold)

PAX(`playax.kr`) 정적 사이트 레포 안의 **실험용 Android 모듈**입니다. GitHub Pages 사이트와 무관하며, 출시 직전에 **전용 레포로 분리**하는 것을 권장합니다.

기획: 프로젝트 스토어 `docs/android-novel-app-plan.md`

## 스택

- Kotlin, Jetpack Compose, Navigation, DataStore
- 번들 원작: `app/src/main/assets/novels/`
- 패키지(임시): `kr.playax.novel`

## 콘텐츠 정책

- **원작 AI 연재만** 번들합니다. 기존 무협/웹소설 스크랩·재배포 금지.
- 성인 장면은 `<!-- adult:start -->` … `<!-- adult:end -->` 마커 + 18+ 게이트.

## 로컬 실행

Android Studio에서 `apps/novel-android`를 열고 Sync 후 Run.
또는 SDK가 있는 환경에서:

```bash
cd apps/novel-android
./gradlew :app:assembleDebug
```

이 클라우드 에이전트 환경에는 Android SDK가 없어 APK 빌드는 검증하지 않았습니다.
