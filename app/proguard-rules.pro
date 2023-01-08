-keepattributes Signature
-keepattributes Exceptions

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-keep class retrofit.** { *; }
-keep interface retrofit.** { *; }

-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }

-keep class com.google.gson.examples.android.model.** { *; }

-keep class dagger.** { *; }
-dontwarn dagger.**
