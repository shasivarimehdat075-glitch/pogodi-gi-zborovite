# Pogodi Gi Zborovite — Version 2.0

## Implemented in this preparation branch
- Android version set to 2.0 (versionCode 3).
- Score, selected category, and level are saved locally with SharedPreferences.
- Failed remote images display an in-app fallback instead of a blank image area.
- Letter count excludes spaces in multi-word answers.
- Macedonian Latin input accepts additional diacritic spellings (č, ć, đ, š, ž).
- Existing launcher icon is retained.

## Current question bank status
The current source contains 6 categories and 228 questions (38 per category).
The agreed target is 1,000 questions in each category (6,000 total). This target is not yet complete.
Each new question must have its own distinct, relevant image; image matching and image loading need separate verification.

## Verification still required
- Run the Android build on GitHub Actions.
- Install and test the APK on a physical Android device.
- Check image correspondence and availability.
- Expand the question bank to 6,000 verified entries.
