-keepattributes *Annotation*,Signature

-dontwarn rx.**
-dontwarn retrofit.**

-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
