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

#[------------------------------Gson----------------------------
#Gson
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
#具体项目中model的包名
-keep class com.healthy.library.model.** { *; }
-keep class com.health.mall.model.**{*;}
-keep class com.health.index.model.**{*;}
-keep class com.health.mine.model.**{*;}
-keep class com.health.faq.mode.**{*;}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontshrink
-dontoptimize
-keep public class android.support.v4.content.FileProvider {*;}
-keepattributes Exceptions,InnerClasses,Signature,InnerInterface
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes EnclosingMethod

-keep class com.ksy.statlibrary.** { *; }
-keep class com.ksyun.media.** { *; }


#-------------------------Annotation-------------------------
-keepattributes *Annotation*
-keep @**annotation** class * {*;}


#-------------------------ARouter-------------------------
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}
-dontwarn javax.lang.model.element.**
# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider

#-------------------------BaseAdapter-------------------------
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}

#-------------------------高德地图-------------------------
#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}
#-------------------------BannerView-------------------------
# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

 #[------------------------------Retrofit----------------------------
 # Retrofit does reflection on generic parameters and InnerClass is required to use Signature.
 -keepattributes Signature, InnerClasses

 # Retain service method parameters when optimizing.
 -keepclassmembers,allowshrinking,allowobfuscation interface * {
     @retrofit2.http.* <methods>;
 }

 # Ignore annotation used for build tooling.
 -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

 # Ignore JSR 305 annotations for embedding nullability information.
 -dontwarn javax.annotation.**

 # Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
 -dontwarn kotlin.Unit

 # Top-level functions that can only be used by Kotlin.
 -dontwarn retrofit2.-KotlinExtensions
 #-------------------------------------------------------------]





 #[------------------------------BaseRecyclerViewAdapter----------------------------
 -keep class com.chad.library.adapter.** {
 *;
 }
 -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
 -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
 -keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
      <init>(...);
 }
 -keep class com.chad.library.adapter.base.entity.**
 #-------------------------------------------------------------]


 #[------------------------------Matisse----------------------------
 -dontwarn com.squareup.picasso.**
 #-------------------------------------------------------------]



 #[------------------------------EventBus----------------------------
 -keepattributes *Annotation*
 -keepclassmembers class * {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }

 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(java.lang.Throwable);
 }
 #[------------------------------wx----------------------------
 -keep class com.tencent.mm.opensdk.** {
     *;
 }

 -keep class com.tencent.wxop.** {
     *;
 }

 -keep class com.tencent.mm.sdk.** {
     *;
 }

# -------------------------------umeng--------------------------------
 -dontshrink
 -dontoptimize
 -dontwarn android.webkit.WebView
 -dontwarn com.umeng.**
 -keep public class javax.**
 -keep public class android.webkit.**
 -dontwarn android.support.v4.**
 -keepattributes Exceptions,InnerClasses,Signature
 -keepattributes *Annotation*
 -keepattributes SourceFile,LineNumberTable

 -keep public interface com.tencent.**
 -keep public interface com.umeng.socialize.**
 -keep public interface com.umeng.socialize.sensor.**
 -keep public interface com.umeng.scrshot.**

 -keep public class com.umeng.socialize.* {*;}


 -keep class com.umeng.scrshot.**
 -keep public class com.tencent.** {*;}
 -keep class com.umeng.socialize.sensor.**
 -keep class com.umeng.socialize.handler.**
 -keep class com.umeng.socialize.handler.*
 -keep class com.umeng.weixin.handler.**
 -keep class com.umeng.weixin.handler.*
 -keep class com.umeng.qq.handler.**
 -keep class com.umeng.qq.handler.*
 -keep class UMMoreHandler{*;}

 -keep class com.tencent.** {*;}
 -dontwarn com.tencent.**
 -keep public class com.umeng.com.umeng.soexample.R$*{
     public static final int *;
 }
 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keep class com.umeng.socialize.impl.ImageImpl {*;}

 -keepnames class * implements android.os.Parcelable {
     public static final ** CREATOR;
 }
 -keepattributes Signature

 # -------------------------------bugly--------------------------------
 -dontwarn com.tencent.bugly.**
 -keep public class com.tencent.bugly.**{*;}
 -keep class android.support.**{*;}
 #-------------------okhttp---------------------------------
 -dontwarn com.squareup.okhttp.**
 -keep class com.squareup.okhttp.**{*;}

 # okhttp
 -keep class okhttp3.** { *; }
 -keep interface okhttp3.** { *; }
 -dontwarn okhttp3.**

 # okio
 -keep class sun.misc.Unsafe { *; }
 -dontwarn java.nio.file.*
 -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
 -keep class okio.**{*;}
 -dontwarn okio.**
 # 这里解决kotlin解析数据问题
 -keepnames class * implements java.io.Serializable
 -keep public class * implements java.io.Serializable {
    public *;
 }
 -keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     !static !transient <fields>;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }

-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.health.client.R$*{
public static final int *;
}