plugins {
    id("com.android.application")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "gr.galeos.seniortracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "gr.galeos.seniortracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val MAPTILER_API_KEY = System.getProperty("MAPTILER_API_KEY")
        buildConfigField("String", "MAPBOX_ACCESS_TOKEN", "$MAPTILER_API_KEY")
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

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"

}


dependencies {

    implementation("com.google.firebase:firebase-firestore:25.1.0")
    val activityVersion = "1.8.2"
    val androidCoreVersion = "1.12.0"
    val appCompatVersion = "1.6.1"
    val materialVersion = "1.11.0"
    val constraintLayoutVersion = "2.1.4"
    val lifecycleVersion = "2.7.0"
    val navigationVersion = "2.7.7"
    val mapboxVersion = "10.18.2 "

    // Android
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.core:core-ktx:$androidCoreVersion")
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Material Version 2
    implementation("com.google.android.material:material:$materialVersion")

    // Constraint Layout
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // ViewModels - Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    // Navigation
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")

    implementation("com.mapbox.navigationcore:navigation:3.3.0-rc.1")
    implementation("com.mapbox.navigationcore:copilot:3.3.0-rc.1")
    implementation("com.mapbox.navigationcore:ui-maps:3.3.0-rc.1")
    implementation("com.mapbox.navigationcore:tripdata:3.3.0-rc.1")
    implementation("com.mapbox.navigationcore:ui-components:3.3.0-rc.1")
    implementation("com.mapbox.navigationcore:android:3.3.0-rc.1")

    //Firebase
    implementation("com.google.firebase:firebase-auth:23.0.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")

    implementation("com.google.android.gms:play-services-maps:19.0.0")

    // Maps SDK for Android
    implementation("com.mapbox.maps:android:11.4.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}