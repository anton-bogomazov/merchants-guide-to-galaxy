#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../../"

(cd "$rootDir" && exec ./tools/scripts/build.sh)
(cd "$rootDir" && export APP_PROFILE=local && export LOG_APPENDER=FILE && exec ./gradlew run --console=plain)
