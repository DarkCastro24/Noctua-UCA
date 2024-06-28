plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.castroll.noctua"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.castroll.noctua"
        minSdk = 24
        targetSdk = 34
        versionCode = 8
        versionName = "8"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.firebase.auth)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Implementaciones para inicio de sesion con Google + Firebase
    implementation (platform("com.google.firebase:firebase-bom:26.0.0"))
    implementation ("com.google.android.gms:play-services-auth:18.1.0")

    // Dependencias de retrofit para consumo de APIS
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("org.json:json:20210307")

    // Dependencia para cargar imagenes
    implementation("io.coil-kt:coil-compose:2.1.0")

    implementation ("androidx.compose.ui:ui:1.2.0")
    implementation ("androidx.compose.material:material:1.2.0")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.2.0")
    implementation ("androidx.activity:activity-compose:1.4.0")


    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation ("androidx.compose.runtime:runtime-livedata:1.0.0-beta01")
    implementation ("com.google.code.gson:gson:2.8.6")


    // ViewModel y LiveData con Compose
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")

    // ViewModel y Navegacion
    implementation ("androidx.compose.ui:ui:1.2.0")
    implementation ("androidx.compose.material3:material3:1.0.0-alpha12")
    implementation ("androidx.compose.ui:ui-tooling:1.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation ("androidx.navigation:navigation-compose:2.5.1")
    implementation ("androidx.compose.material3:material3:1.0.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.0.0")

    // QR AND BAR CODE
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("androidx.compose.material3:material3:1.0.0")

    //Video fondo
    implementation ("io.coil-kt:coil-compose:2.1.0")

    // Envio de correo electronico
    implementation ("com.sun.mail:android-mail:1.6.5")
    implementation ("com.sun.mail:android-activation:1.6.5")

}