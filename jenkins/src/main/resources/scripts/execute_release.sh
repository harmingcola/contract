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

echo "Updating versions for release"
mvn versions:set -DnewVersion="$RELEASE_VERSION" -DgenerateBackupPoms=false || exit 1
git add -A && git commit -m "Updating version to $RELEASE_VERSION for release" || exit 1
git push --set-upstream origin master || exit 1

mvn deploy || exit 1

echo "Updating to $NEXT_VERSION for development"
mvn versions:set -DnewVersion="$NEXT_VERSION" -DgenerateBackupPoms=false || exit 1
git add -A && git commit -m "Updating version to $NEXT_VERSION" || exit 1
git push --set-upstream origin master || exit 1