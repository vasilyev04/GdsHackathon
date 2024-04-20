plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.vasilyev.gdshackathon"
    compileSdk = 34

    buildTypes {
        release {
            val apiKey = properties["MAPS_API_KEY"].toString()
            buildConfigField("String", "API_KEY", apiKey)
        }
    }

    defaultConfig {
        applicationId = "com.vasilyev.gdshackathon"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true;
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    //Google Map
    implementation (libs.google.play.services.maps)

    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //ViewModel
    implementation (libs.androidx.lifecycle.extensions)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.fragment.ktx)

    //Kotlin Result API for Retrofit
    implementation (libs.retrofit.adapters.result)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}