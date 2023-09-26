import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_7

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.tpfinaltusi"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.tpfinaltusi"
        minSdk = 26
        targetSdk = 33
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
        //sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
        //targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
    }
    packaging {
        // Exclude the problematic files
        //resources.excludes.add("META-INF/INDEX.LIST")
        //resources.excludes.add("META-INF/io.netty.versions.properties")
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.postgresql:postgresql:42.2.9")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}

