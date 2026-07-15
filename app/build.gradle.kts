plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.erazero1.tasks"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.erazero1.tasks"
        minSdk = 24
        targetSdk = 36
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Android
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    implementation(libs.kotlinx.serialization.json)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)



    // Network
    implementation(libs.ok.http.client)
    implementation(libs.http.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.converter)

    // Dependency Injection
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}