plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")


// Correct way to apply the Google services plugin
}


android {
    namespace = "com.example.signin_signup"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.signin_signup"  // Make sure this matches the package name
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    // Kotlin Standard Library - Use only one version
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21")  // Prefer JDK8 variant if needed

    // Firebase dependencies
    implementation("com.google.firebase:firebase-firestore:24.3.0")
    implementation("com.google.firebase:firebase-auth:21.0.0")

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Core Android dependencies
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("com.google.android.material:material:1.9.0")



    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:1.8.21")  // Ensure only one Kotlin version is used
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21")  // Same version for JDK8 variant
    }
}



