plugins {
    id("com.android.library")
    id("maven-publish")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "jack.rcyc.labeltransliterator"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testOptions.targetSdk = 35
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

    // Optional: if you're publishing a release variant
    publishing {
        singleVariant("release")
    }
}


publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "io.github.rcycjack" // Replace with your GitHub username or org
            artifactId = "labeltransliterator"
            version = "0.3.0"

            // Important: publish Android library AAR, not Java jar
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}