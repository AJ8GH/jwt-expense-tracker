#!/bin/sh

curl 'http://localhost:8080/api/auth' \
-H 'Content-Type: application/json' \
-H 'Accept: application/json' \
-d '{
  "username": "un2",
  "password": "pw"
}'
