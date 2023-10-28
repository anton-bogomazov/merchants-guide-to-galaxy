#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../../"

(cd "$rootDir" && exec docker-compose -f ./tools/docker/docker-compose.yml --env-file \
./tools/docker/env/local.env --project-name=merchants-guide --profile local up -d --remove-orphans)

portainerPort=$(cd "$rootDir" && cat ./tools/docker/env/local.env | grep "PORTAINER_PORT" | cut -d'=' -f2)

printf 'List of available ports\n'
(cd "$rootDir" && exec cat ./tools/docker/env/local.env)
printf "\nPortainer GUI is available at http://localhost:$portainerPort/#/dashboard\n"
printf 'Pgadmin Login: merchant@abogomazov.com:pass\nDatabase password is pass'
python3.9 -mwebbrowser http://localhost:$portainerPort/#/dashboard || true
