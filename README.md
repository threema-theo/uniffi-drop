# Reproducer for a UniFFI bug

This is a reproducer for https://github.com/mozilla/uniffi-rs/issues/2600

Running the code:

1. Requirements: x86_64, Android Developer tools (e.g., `gradle`), Rust
2. `cd kotlin`
3. Run `./gradlew test -i  --rerun-tasks` and observe the output
