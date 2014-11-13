-keepattributes *Annotation*,Signature

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**

-keep class okio.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
