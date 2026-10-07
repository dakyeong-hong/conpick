#!/bin/bash
# 사용법: ./test-auth.sh <브라우저에서_복사한_refresh_token>

BASE="http://localhost:8080"
JAR="cookies.txt"
FIRST_TOKEN="$1"

if [ -z "$FIRST_TOKEN" ]; then
  echo "사용법: ./test-auth.sh <refresh_token>"
  exit 1
fi
rm -f "$JAR"

echo "== 1. 재발급 (기대: 200 + accessToken) =="
RES=$(curl -s -w "\n%{http_code}" -X POST "$BASE/auth/refresh" \
  -b "refresh_token=$FIRST_TOKEN" -c "$JAR")
echo "$RES"
ACCESS=$(echo "$RES" | sed -n -E 's/.*"accessToken":"([^"]+)".*/\1/p')

echo
echo "== 2. 새 Access Token으로 API 호출 (기대: 200 + 내 정보) =="
curl -s -w "\n%{http_code}\n" "$BASE/users/me" -H "Authorization: Bearer $ACCESS"

echo
echo "== 3. 이미 쓴 Refresh Token 재사용 (기대: 실패) =="
curl -s -o /dev/null -w "%{http_code}\n" -X POST "$BASE/auth/refresh" \
  -b "refresh_token=$FIRST_TOKEN"

echo
echo "== 4. 회전된 새 토큰으로 재발급 (기대: 200) =="
curl -s -o /dev/null -w "%{http_code}\n" -X POST "$BASE/auth/refresh" \
  -b "$JAR" -c "$JAR"

# 로그아웃 후 검증에 쓰려고 현재 토큰을 따로 보관
CURRENT=$(awk '$6=="refresh_token"{print $7}' "$JAR")

echo
echo "== 5. 로그아웃 (기대: 204) =="
curl -s -o /dev/null -w "%{http_code}\n" -X POST "$BASE/auth/logout" \
  -b "$JAR" -c "$JAR"

echo
echo "== 6. 로그아웃된 토큰으로 재발급 (기대: 실패) =="
curl -s -o /dev/null -w "%{http_code}\n" -X POST "$BASE/auth/refresh" \
  -b "refresh_token=$CURRENT"

echo
echo "== 7. 쿠키 없이 재발급 (기대: 400) =="
curl -s -o /dev/null -w "%{http_code}\n" -X POST "$BASE/auth/refresh"

rm -f "$JAR"
