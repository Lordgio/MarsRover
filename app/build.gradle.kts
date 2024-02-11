plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "dev.jorgeroldan.marsrover"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.jorgeroldan.marsrover"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "DebugProbesKt.bin"
        }
    }
}

dependencies {

    // Internal modules
    implementation(project(":ui"))
    implementation(project(":domain"))
    implementation(project(":data"))

    // Android
    implementation(libs.androidx.activity.compose)

    // DI
    implementation(libs.koin.android)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
}