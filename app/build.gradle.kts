val trackingIntervalDebug: String = "10000L" //
val trackingIntervalRelease: String = "600000L"
val trackingIntervalMetersDebug: String = "60"  //10
val trackingIntervalMetersRelease: String = "60"

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gpstracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gpstracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    viewBinding {
        enable = true
    }

    buildFeatures {
        buildConfig = true
    }


    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            buildConfigField("Long", "TRACKING_INTERVAL_MILLIS", trackingIntervalRelease)
            buildConfigField("int", "TRACKING_INTERVAL_METERS", trackingIntervalMetersRelease)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            buildConfigField("Long", "TRACKING_INTERVAL_MILLIS", trackingIntervalDebug)
            buildConfigField("int", "TRACKING_INTERVAL_METERS", trackingIntervalMetersDebug)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0-alpha02"
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
        jniLibs {
            useLegacyPackaging = true
        }
    }

}



dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")


    //Test
/*    androidTestImplementation("junit:junit:4.13.2")
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("io.mockk:mockk-android:1.13.7")
    debugImplementation("androidx.test:core:1.5.0")
    debugImplementation("androidx.test:runner:1.5.2")
    debugImplementation("androidx.test:rules:1.5.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")
    debugImplementation ("android.arch.core:core-testing:1.1.1")
    debugImplementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.test.espresso:espresso-intents:3.5.1")
    implementation("androidx.test.espresso:espresso-core:3.5.1")*/

    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    implementation("io.mockk:mockk-android:1.13.7")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.ext:truth:1.5.0")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")
    debugImplementation ("android.arch.core:core-testing:1.1.1")
    debugImplementation ("androidx.fragment:fragment-ktx:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //Dagger
    implementation("com.google.dagger:dagger:2.48")
    implementation("com.google.dagger:dagger-android")//
    implementation("com.google.dagger:dagger-android-support:2.48") //
    kapt("com.google.dagger:dagger-compiler:2.48")
    kapt("com.google.dagger:dagger-android-processor:2.48") //

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.2")
    debugImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.6.4")

    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Compose
    implementation("androidx.compose.ui:ui:1.5.1")  //1.4.0
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation("androidx.compose.animation:animation:1.5.1")
    implementation("androidx.compose.ui:ui-tooling:1.5.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.1")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.navigation:navigation-compose:2.7.3")

    //Navigation
    implementation("androidx.navigation:navigation-ui-ktx:2.7.3")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.3")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.3")

    //Maps
    implementation("com.google.maps.android:maps-compose:2.5.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.maps.android:maps-utils-ktx:3.4.0")
    implementation("com.google.maps.android:maps-ktx:3.2.1")

    //Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //Room
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    //Firebase
    //implementation (platform("com.google.firebase:firebase-bom:31.4.0"))
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")

    //WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    //Datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")


}