#!/usr/bin/env sh

diplomat-tool -e ../rust/src/lib.rs -c ../rust/config.toml kotlin out
cp out/src/main/kotlin/uniffi/librust/librust/*.kt app/src/main/java/uniffi/librust/
rm -r out
