#!/usr/bin/env bash

set -euxo pipefail

rm output/main.js ||:
rm output/package.json ||:
rm output/preload.js ||:
rm output/renderer.js ||:
rm output/README.md ||:

sbt electronOutput

electron output