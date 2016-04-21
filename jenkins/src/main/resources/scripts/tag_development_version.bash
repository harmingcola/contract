#!/usr/bin/env bash

MASTER_VERSION=$(xmlstarlet sel -N x="http://maven.apache.org/POM/4.0.0" -t -v '/x:project/x:version' pom.xml)
IFS='.|-' read -ra version_tokens <<< "$MASTER_VERSION"
MAJOR_VERSION=${version_tokens[0]}
MINOR_VERSION=${version_tokens[1]}
INCREMENTAL_VERSION=${version_tokens[2]}

NEXT_INCREMENTAL=$(($INCREMENTAL_VERSION + 1))

TAG_VERSION="$MAJOR_VERSION.$MINOR_VERSION.$INCREMENTAL_VERSION"
NEXT_VERSION="$MAJOR_VERSION.$MINOR_VERSION.$NEXT_INCREMENTAL-SNAPSHOT"

echo "Versions for tagging"
echo "Current : $MASTER_VERSION"
echo "Tag     : $TAG_VERSION"
echo "Next    : $NEXT_VERSION"

echo "Updating versions for tag"
mvn versions:set -DnewVersion="$TAG_VERSION" -DgenerateBackupPoms=false
git add -A && git commit -m "Tagging at $TAG_VERSION"
git push --set-upstream origin master

git tag "$TAG_VERSION" -lw

echo "Updating to $NEXT_VERSION for development"
mvn versions:set -DnewVersion="$NEXT_INCREMENTAL" -DgenerateBackupPoms=false
git add -A && git commit -m "Updating version to $NEXT_VERSION"
git push