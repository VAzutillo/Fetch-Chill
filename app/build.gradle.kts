plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.fetchchill"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fetchchill"
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
        buildFeatures {
            viewBinding = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //http client
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)

    //retrofit and json response converter
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

//    implementation(libs.glide)
//    glide = { module = "com.github.bumptech.glide:glide", version.ref = "glide" }
    implementation(libs.glide)




    }