# Arabian Nights Android App

This repository is prepared for building the Arabian Nights Android APK with GitHub Actions.

## Required source files
Upload the **contents** of `ArabianNights_GitHub_APK_Ready.zip` into the repository root (not the ZIP itself). The Android project must contain `settings.gradle`, `build.gradle`, `app/build.gradle`, and `app/src/main/`.

## Build
After the Android project source is present, open **Actions > Build Arabian Nights Android APK > Run workflow**.
After a successful run, download the `ArabianNights-Android-APK` artifact and extract `app-debug.apk`.

This debug APK is for testing, not Play Store release. No build has been verified yet.
