#!/bin/bash

set -e

REPO="git@github.com:ReactiveCircus/blueprint.git"
REMOTE_NAME="origin"
DIR=temp-clone

if [ -n "${CI}" ]; then
  REPO="https://github.com/${GITHUB_REPOSITORY}.git"
fi

# Clone project into a temp directory
rm -rf $DIR
git clone "$REPO" $DIR
cd $DIR

# Generate API docs
./gradlew dokka

# Copy *.md files into docs directory
cp README.md docs/index.md
mkdir -p docs/samples && cp samples/README.md docs/samples/index.md
mkdir -p docs/samples/demo-coroutines && cp samples/demo-coroutines/README.md docs/samples/demo-coroutines/index.md
mkdir -p docs/samples/demo-rx && cp samples/demo-rx/README.md docs/samples/demo-rx/index.md
mkdir -p docs/samples/demo-common && cp samples/demo-common/README.md docs/samples/demo-common/index.md
mkdir -p docs/samples/demo-testing-common && cp samples/demo-testing-common/README.md docs/samples/demo-testing-common/index.md
mkdir -p docs/blueprint-interactor-coroutines && cp blueprint-interactor-coroutines/README.md docs/blueprint-interactor-coroutines/index.md
mkdir -p docs/blueprint-interactor-rx2 && cp blueprint-interactor-rx2/README.md docs/blueprint-interactor-rx2/index.md
mkdir -p docs/blueprint-interactor-rx3 && cp blueprint-interactor-rx3/README.md docs/blueprint-interactor-rx3/index.md
mkdir -p docs/blueprint-async-coroutines && cp blueprint-async-coroutines/README.md docs/blueprint-async-coroutines/index.md
mkdir -p docs/blueprint-async-rx2 && cp blueprint-async-rx2/README.md docs/blueprint-async-rx2/index.md
mkdir -p docs/blueprint-async-rx3 && cp blueprint-async-rx3/README.md docs/blueprint-async-rx3/index.md
mkdir -p docs/blueprint-ui && cp blueprint-ui/README.md docs/blueprint-ui/index.md
mkdir -p docs/blueprint-testing-robot && cp blueprint-testing-robot/README.md docs/blueprint-testing-robot/index.md
cp CHANGELOG.md docs/changelog.md

# If on CI, configure git remote with access token
if [ -n "${CI}" ]; then
  REMOTE_NAME="https://x-access-token:${DEPLOY_TOKEN}@github.com/${GITHUB_REPOSITORY}.git"
  git config --global user.name "${GITHUB_ACTOR}"
  git config --global user.email "${GITHUB_ACTOR}@users.noreply.github.com"
  git remote add deploy "$REMOTE_NAME"
  git fetch deploy && git fetch deploy gh-pages:gh-pages
fi

# Build the website and deploy to GitHub Pages
mkdocs gh-deploy --remote-name "$REMOTE_NAME"

# Delete temp directory
cd ..
rm -rf $DIR
