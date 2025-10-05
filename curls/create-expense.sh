#!/bin/sh

curl 'http://localhost:8080/api/expenses' \
-H 'Content-Type: application/json' \
-H 'Accept: application/json' \
-H "Authorization: Bearer $1" \
-d '{
  "category": "FOOD",
  "amount": "200.50",
  "date": "2020-07-26",
  "description": "Weekly shop"
}'
