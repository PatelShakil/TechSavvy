plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.shakilpatel.techsavvy"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.shakilpatel.techsavvy"
        minSdk = 26
        targetSdk = 36
        versionCode = 4
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //navigation
    implementation (libs.androidx.navigation.runtime.ktx)
    implementation (libs.androidx.navigation.compose)

    //dependency injection
    implementation (libs.androidx.hilt.navigation.compose)
    implementation (libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)

    //image loading library from url
    implementation(libs.coil.compose)

    //zoomable image
    implementation (libs.zoomable)

    // Paging Compose
    implementation (libs.accompanist.pager.indicators)
    implementation(libs.accompanist.pager)

    //Firebase
    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.analytics.ktx)
    implementation (libs.firebase.firestore.ktx)
    implementation (libs.firebase.database.ktx)
    implementation (libs.firebase.auth.ktx)
    implementation (libs.firebase.storage.ktx)
    implementation (libs.firebase.messaging.ktx)
    implementation (libs.firebase.crashlytics.ktx)

    implementation(libs.androidx.material.icons.extended)
    implementation("androidx.compose.foundation:foundation:1.6.0")


}
