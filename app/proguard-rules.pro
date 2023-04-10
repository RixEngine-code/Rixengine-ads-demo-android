# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.alxad.**
-dontwarn com.admob.custom.adapter.**
-dontwarn com.anythink.custom.adapter.**
-dontwarn com.ironsource.adapters.custom.algorix.**
-dontwarn com.applovin.mediation.adapters.**
-dontwarn com.mopub.mobileads.**
-dontwarn com.tradplus.custom.adapter.**

-keep class com.alxad.api.** {*;}
-keep class com.admob.custom.adapter.** {*;}
-keep class com.anythink.custom.adapter.** {*;}
-keep class com.ironsource.adapters.custom.algorix.** {*;}
-keep class com.mopub.mobileads.** {*;}
-keep class com.mopub.mobileads.** {*;}
-keep class com.tradplus.custom.adapter.** {*;}

#topon平台，如果没用到此平台可以不用
-keep class com.anythink.** {*;}
-keep public class com.anythink.network.**
-keepclassmembers class com.anythink.network.** {
   public *;
}
-dontwarn com.anythink.hb.**
-keep class com.anythink.hb.**{ *;}
-dontwarn com.anythink.china.api.**
-keep class com.anythink.china.api.**{ *;}
-keep class com.anythink.myoffer.ui.**{ *;}
-keepclassmembers public class com.anythink.myoffer.ui.** {
   public *;
}

#tradplus平台，如果没用到此平台可以不用加
-keep public class com.tradplus.** { *; }
-keep class com.tradplus.ads.** { *; }