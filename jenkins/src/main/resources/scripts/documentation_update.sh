#!/usr/bin/env bash

set -x

mvn site -f documentation/pom.xml

DOC_TEMP_DIR=$(mktemp -d)
cp -a documentation/target/site/* "$DOC_TEMP_DIR"

git checkout gh-pages
cp "$$DOC_TEMP_DIR/*" .
git add -A
git commit -m "Documentation update"
git push 