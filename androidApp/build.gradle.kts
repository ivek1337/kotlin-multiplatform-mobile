import java.io.File
import java.util.*

val keystoreProperties =
        Properties().apply {
            var file = File("androidApp/key.properties")
            if (file.exists()) load(file.reader())
        }

plugins {
//    id("com.android.application")
//    kotlin("android")
    id("com.android.application").version("8.1.0").apply(false)
    id("com.android.library").version("8.1.0").apply(false)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin").version("2.0.1").apply(false)
    kotlin("android").version("1.9.10").apply(false)
    kotlin("multiplatform").version("1.9.10").apply(false)
    kotlin("plugin.serialization").version("1.9.10").apply(false)
}

android {
    compileSdk = 31
    val appVersionCode = (System.getenv()["NEW_BUILD_NUMBER"] ?: "1")?.toInt()
    defaultConfig {
        applicationId = "io.codemagic.ivan.kmm"
        minSdk = 21
        targetSdk = 31
        versionCode = appVersionCode
        versionName = "1.0"
    }
    signingConfigs {
        create("release") {
            if (System.getenv()["CI"].toBoolean()) { // CI=true is exported by Codemagic
                storeFile = file(System.getenv()["CM_KEYSTORE_PATH"])
                storePassword = System.getenv()["CM_KEYSTORE_PASSWORD"]
                keyAlias = System.getenv()["CM_KEY_ALIAS"]
                keyPassword = System.getenv()["CM_KEY_PASSWORD"]
            } else {
                storeFile = file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

// dependencies {
//     implementation(project(":shared"))
//     implementation("com.google.android.material:material:1.4.0")
//     implementation("androidx.appcompat:appcompat:1.3.1")
//     implementation("androidx.constraintlayout:constraintlayout:2.1.0")
// }
