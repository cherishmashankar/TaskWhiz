plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.taskwhiz"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.taskwhiz"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    // UI - Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // Architecture
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.datastore.preferences)

    // Dependency Injection
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.hilt.common)
    kapt(libs.hilt.compiler)

    // Data - Local
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Data - Remote
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // Async
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.leakcanary)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.room.testing)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.truth)

    // Android Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.test.ext.junit.old)
    androidTestImplementation(libs.androidx.test.core)
}