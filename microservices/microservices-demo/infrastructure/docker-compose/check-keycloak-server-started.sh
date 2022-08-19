#!/bin/bash
# check-keycloak-server-started.sh

curlResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://keycloak-authorization-server:9091/auth/realms/microservices-realm)

echo "result status code:" "$curlResult"

while [[ ! $curlResult == "200" ]]; do
  >&2 echo "Keycloak server is not up yet!"
  sleep 2
  curlResult=$(curl -s -o /dev/null -I -w "%{http_code}" http://keycloak-authorization-server:9091/auth/realms/microservices-realm)
done

/cnb/process/web