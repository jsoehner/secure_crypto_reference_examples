#!/usr/bin/env bash
set -euo pipefail
rm -rf out
mkdir -p out
find src/main/java -name '*.java' -print0 | xargs -0 javac -d out
for source in src/main/java/org/example/crypto/*Example.java; do
  class_name="$(basename "$source" .java)"
  java -cp out "org.example.crypto.${class_name}"
done
