#!/usr/bin/env bash
# Create a local upload keystore for Play App Signing (do NOT commit the .jks).
# Usage (from apps/novel-android):
#   ./scripts/create-upload-keystore.sh
# Windows (Git Bash / WSL): same script, or see docs/build-aab.md

set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

KEYSTORE_FILE="${KEYSTORE_FILE:-upload-keystore.jks}"
ALIAS="${KEY_ALIAS:-pax-novel-upload}"
PROPS_FILE="${PROPS_FILE:-keystore.properties}"

if [[ -f "$KEYSTORE_FILE" ]]; then
  echo "Refusing to overwrite existing $KEYSTORE_FILE"
  exit 1
fi

if [[ -f "$PROPS_FILE" ]]; then
  echo "Refusing to overwrite existing $PROPS_FILE"
  exit 1
fi

if [[ -z "${STORE_PASSWORD:-}" || -z "${KEY_PASSWORD:-}" ]]; then
  echo "Set STORE_PASSWORD and KEY_PASSWORD env vars (use a password manager)."
  echo "Example:"
  echo "  STORE_PASSWORD='...' KEY_PASSWORD='...' ./scripts/create-upload-keystore.sh"
  exit 1
fi

keytool -genkeypair \
  -v \
  -storetype JKS \
  -keystore "$KEYSTORE_FILE" \
  -alias "$ALIAS" \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass "$STORE_PASSWORD" \
  -keypass "$KEY_PASSWORD" \
  -dname "CN=PAX Novel Upload, OU=PlayAX, O=PAX, L=Seongnam, ST=Gyeonggi, C=KR"

cat > "$PROPS_FILE" <<EOF
storeFile=$KEYSTORE_FILE
storePassword=$STORE_PASSWORD
keyAlias=$ALIAS
keyPassword=$KEY_PASSWORD
EOF

echo "Created $KEYSTORE_FILE and $PROPS_FILE (both gitignored)."
echo "Back up the keystore and passwords offline. Never commit them."
