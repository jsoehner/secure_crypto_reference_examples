#!/usr/bin/env bash
set -euo pipefail
# Get the directory where this script is located
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
rm -rf "$DIR/out"
mkdir -p "$DIR/out"
find "$DIR/src/main/java" -name '*.java' -print0 | xargs -0 javac -d "$DIR/out"
for source in "$DIR/src/main/java/org/example/crypto/"*Example.java; do
  class_name="$(basename "$source" .java)"
  java -cp "$DIR/out" "org.example.crypto.${class_name}"
done
