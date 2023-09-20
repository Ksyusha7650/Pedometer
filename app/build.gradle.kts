plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.pedometer"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.pedometer"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
/*    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")*/
    implementation ("com.github.blackfizz:eazegraph:1.2.5l@aar")
    implementation ("com.nineoldandroids:library:2.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // Preferences DataStore (SharedPreferences like APIs)

        implementation("androidx.datastore:datastore-preferences:1.0.0")

        // optional - RxJava2 support
        implementation("androidx.datastore:datastore-preferences-rxjava2:1.0.0")

        // optional - RxJava3 support
        implementation("androidx.datastore:datastore-preferences-rxjava3:1.0.0")

        implementation("com.github.lecho:hellocharts-library:1.5.8@aar")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}