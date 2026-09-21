# PlayAX Novel (Android scaffold)

PAX(`playax.kr`) 정적 사이트 레포 안의 **실험용 Android 모듈**입니다. GitHub Pages 사이트와 무관하며, 출시 직전에 **전용 레포로 분리**하는 것을 권장합니다.

기획: 프로젝트 스토어 `docs/android-novel-app-plan.md`

## 스택

- Kotlin, Jetpack Compose, Navigation, DataStore, **Room** (집필 영속화)
- 번들 원작: `app/src/main/assets/novels/`
- 패키지(확정): `kr.playax.novel` (`applicationId` / `namespace`)
- 런처 표시명: **PAX Novel**

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

이 모듈은 Room으로 작품/회차/본문을 로컬 DB에 저장합니다. 에뮬레이터·SDK는 환경에 따라 별도 설치가 필요합니다.

## Play용 AAB (release)

1. `keystore.properties.example`을 `keystore.properties`로 복사하고 로컬 값 입력  
   (또는 `STORE_PASSWORD=… KEY_PASSWORD=… ./scripts/create-upload-keystore.sh`)  
2. **비밀번호·`.jks`는 커밋하지 마세요** (`keystore.properties`, `*.jks`는 gitignore)  
3. 빌드:

```bat
REM Windows
cd apps\novel-android
gradlew.bat :app:bundleRelease
```

```bash
# macOS / Linux / Git Bash
cd apps/novel-android
./gradlew :app:bundleRelease
```

성공 후 산출물:

```text
app/build/outputs/bundle/release/app-release.aab
```

레포 기준 상대 경로: `apps/novel-android/app/build/outputs/bundle/release/app-release.aab`  
이 경로는 **빌드 성공 후에만** 존재합니다.
