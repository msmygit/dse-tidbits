#!/bin/bash

echo "Starting DSE 6.8.7...."
docker-compose --file stargatedse68.yml up --detach backend-1
(docker-compose --file stargatedse68.yml logs -f backend-1 &) | grep -q "DSE startup complete."

echo ""
echo "Starting Stargate..."
docker-compose --file stargatedse68.yml up --detach stargate
echo "Sleeping for 60 seconds..."
sleep 60

echo ""
echo "Waiting for Stargate to start up..."
while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' http://localhost:8082/health)" != "200" ]]; do
    printf '.'
    sleep 5
done

curl -L -X POST 'http://localhost:8081/v1/auth' \
  -H 'Content-Type: application/json' \
  --data-raw '{
    "username": "cassandra",
    "password": "cassandra"
}'

echo ""
echo "Done!"