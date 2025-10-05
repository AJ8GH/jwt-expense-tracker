#!/bin/sh

curl 'http://localhost:8080/api/auth/refresh' \
-H 'Content-Type: application/json' \
-H 'Accept: application/json' \
-d "{
  \"refreshToken\": \"$1\",
}"
