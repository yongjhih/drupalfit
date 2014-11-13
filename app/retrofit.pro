-keepattributes *Annotation*,Signature

-dontwarn retrofit.**

-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
