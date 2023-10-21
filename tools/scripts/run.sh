#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../../"

(cd "$rootDir" && exec ./tools/scripts/build.sh)
(cd "$rootDir" && exec ./gradlew run --console=plain)
