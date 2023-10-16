#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec ./gradlew build -PallWarningsAsErrors=true)
(cd "$rootDir" && exec ./gradlew test)
(cd "$rootDir" && exec ./gradlew run)
