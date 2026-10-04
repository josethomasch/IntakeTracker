plugins {

    id("com.android.application")

    id("org.jetbrains.kotlin.android")

    id("kotlin-kapt")

}



android {

    namespace = "com.example.intaketracker"

    compileSdk = 34



    defaultConfig {

        applicationId = "com.example.intaketracker"

        minSdk = 26

        targetSdk = 34

        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }



    buildFeatures {

        compose = true

    }

    composeOptions {

        kotlinCompilerExtensionVersion = "1.5.8"

    }

}



dependencies {

    implementation("androidx.room:room-runtime:2.6.1")

    implementation("androidx.room:room-ktx:2.6.1")

    kapt("androidx.room:room-compiler:2.6.1")



    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    implementation("androidx.compose.ui:ui")

    implementation("androidx.compose.material3:material3")

    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")



    testImplementation("junit:junit:4.13.2")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    testImplementation("app.cash.turbine:turbine:1.0.0")

    testImplementation("androidx.arch.core:core-testing:2.2.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")

}

