object PluginDependencies {
    val android = "com.android.tools.build:gradle:${Versions.gradleAndroidPlugin}"
    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object ProjectDependencies {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"
    val interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
    val rxBinding = "com.jakewharton.rxbinding2:rxbinding:${Versions.rxbinding}"
    val rxBindingSupport = "com.jakewharton.rxbinding2:rxbinding-support-v4:${Versions.rxbinding}"
    val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}"
    val circleImageView = "de.hdodenhof:circleimageview:${Versions.circleImageView}"
    val retrofit =  "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val retrofitMoshi =  "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    val moshiAdapters = "com.squareup.moshi:moshi-adapters:${Versions.moshi}"
    val moshiKotlin =  "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    val koin =  "org.koin:koin-android:${Versions.koin}"
    val koinViewModel = "org.koin:koin-android-viewmodel:${Versions.koin}"
    val junit = "junit:junit:${Versions.junit}"
    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val material = "com.google.android.material:material:${Versions.material}"
    val audioStreamer =  "com.github.chyrta:DMAudioStreamer:${Versions.audioStreamer}"
    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    val supportLegacy = "androidx.legacy:legacy-support-v4:${Versions.supportLegacy}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
}

object Versions {
    val gradleAndroidPlugin = "3.4.1"

    val buildTools = "28.0.3"
    val compileSdk = 28
    val targetSdk = 28
    val minSdk = 16
    val releaseVersionCode = 1
    val releaseVersionName = "1.0"

    val interceptor = "4.0.0"
    val rxbinding = "2.2.0"
    val glide = "4.9.0"
    val moshi = "1.8.0"
    val circleImageView = "3.0.0"
    val appCompat = "1.0.2"
    val supportLegacy = "1.0.0"
    val recyclerView = "1.0.0"
    val constraintLayout = "1.1.3"
    val material = "1.0.0"
    val audioStreamer = "hotfix~notification-SNAPSHOT"
    val retrofit = "2.6.0"
    val rxjava = "2.2.9"
    val rxandroid = "2.1.1"
    val kotlin = "1.3.31"
    val ktlint = "0.33.0"
    val koin = "2.0.1"
    val junit = "4.12"
    val timber = "4.7.1"

}