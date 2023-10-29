#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../../"

liquibaseChangelogPath="application/postgres-storage/src/main/resources"
(cd "$rootDir$liquibaseChangelogPath" &&
exec liquibase update-sql --url=offline:postgres?outputLiquibaseSql=none \
--changelog-file="master.yml" \
--output-file="$rootDir"/db-migration.sql)
(cd "$rootDir$liquibaseChangelogPath" && rm databasechangelog.csv)
