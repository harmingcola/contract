#!/usr/bin/env bash

MASTER_VERSION=$(xmlstarlet sel -N x="http://maven.apache.org/POM/4.0.0" -t -v '/x:project/x:version' pom.xml)
IFS='.|-' read -ra version_tokens <<< "$MASTER_VERSION"
MAJOR_VERSION=${version_tokens[0]}
MINOR_VERSION=${version_tokens[1]}
INCREMENTAL_VERSION=${version_tokens[2]}
DEVELOPMENT_VERSION=$(($INCREMENTAL_VERSION + 1))

RELEASE_VERSION="$MAJOR_VERSION.$MINOR_VERSION.$INCREMENTAL_VERSION"
NEXT_VERSION="$MAJOR_VERSION.$MINOR_VERSION.$DEVELOPMENT_VERSION-SNAPSHOT"

echo "Versions for release"
echo "Current : $MASTER_VERSION"
echo "Release : $RELEASE_VERSION"
echo "Next    : $NEXT_VERSION"

git push --set-upstream origin master

echo "Updating versions for release"
mvn versions:set -DnewVersion="$RELEASE_VERSION"
find . | grep versionsBackup | xargs rm
git add -A && git commit "Updating version to $RELEASE_VERSION for release"
git push

mvn deploy

echo "Updating to $NEXT_VERSION for development"
mvn versions:set -DnewVersion="$DEVELOPMENT_VERSION"
find . | grep versionsBackup | xargs rm
git add -A && git commit "Updating version to $DEVELOPMENT_VERSION"
git push