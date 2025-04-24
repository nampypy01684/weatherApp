
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.namvox.weather_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.namvox.weather_app"
        minSdk = 24
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

    // Koin

    
        implementation("io.insert-koin:koin-android:3.5.0") // Phiên bản mới nhất
        implementation("io.insert-koin:koin-core:3.5.0")


// Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

// Coil
    implementation ("io.coil-kt:coil-compose:2.6.0")

// System UI Controller
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

// Navigation
    implementation ("androidx.navigation:navigation-compose:2.7.7")

// Material Icons Extended
    implementation ("androidx.compose.material:material-icons-extended:1.6.8")

    implementation ("androidx.core:core-ktx:1.10.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.gms:play-services-location:21.0.1")


//
//        implementation(platform("androidx.compose:compose-bom:2023.10.00")) // Cập nhật BOM Compose
//        implementation("androidx.compose.material3:material3:1.0.0") // Cập nhật Material3
//        implementation("androidx.compose.ui:ui:1.6.0") // Cập nhật UI
//        implementation("androidx.compose.ui:ui-tooling-preview:1.6.0") // Cập nhật UI Tooling
//        // Các dependencies khác
}