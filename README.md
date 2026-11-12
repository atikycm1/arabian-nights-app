# Arabian Nights · Scent Studio

Android fragrance collection app for Arabian Nights.

## Included
- Original Arabian Nights AN logo in the header and Android launcher icon
- Modern, minimal three-slide home carousel
- Day / Night mode with readable contrast and matching Android system bars
- 122 fragrance entries with 1 ml pricing and provisional 3, 6, 12 ml pricing
- Fragrance search, categories, sorting, and rule-based Scent Finder
- In-app Collection Manager with new attar entry, name/price/notes editing, custom-item deletion
- Local persistence and JSON export/import

## Build APK
The Android app source is already in this repository. The Android app loads the bundled, offline HTML experience from `app/src/main/assets/index.html`.

Go to **Actions → Build Arabian Nights Android APK**. The workflow runs on pushes to main and can be started manually. On a successful build it creates an APK artifact, and attempts to publish `ArabianNights.apk` under **Releases**.

GitHub Actions: https://github.com/atikycm1/arabian-nights-app/actions

Releases: https://github.com/atikycm1/arabian-nights-app/releases

## Limitations
- A successful APK build and an on-device test have not yet been independently verified.
- This is a debug/testing APK, not a Play Store release.
- New entries are saved **on the device**, not synchronized to other customers' devices.
- There is no secure admin login, cloud sync, or live AI service in this edition.
- Fragrance notes are left unverified unless the owner marks them verified.
- Price calculations for 3/6/12 ml are provisional; confirm before use.
- Back up your collection before uninstalling the app or clearing its data.

## Branding
The supplied screenshot-derived AN logo is used, not a new logo. The Android launcher resource is a downscaled copy of that same source logo.

## Local build
Open this Gradle project with Android Studio or run `gradle assembleDebug` with Java 17 and Android SDK 35.
