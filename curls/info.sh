#!/bin/sh

curl 'http://localhost:8080/api/actuator/info' \
-H 'Content-Type: application/json' \
-H 'Accept: application/json' \
-H "Authorization: Bearer $1"
