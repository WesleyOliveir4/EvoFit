import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val publishVersionFile = rootProject.file("publish-version.properties")
val publishVersionProperties = Properties().apply {
    if (publishVersionFile.exists()) {
        publishVersionFile.inputStream().use { load(it) }
    }
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use { load(it) }
    }
}

val pubVersionCode = (publishVersionProperties["versionCode"] as? String)?.toIntOrNull() ?: 1
val pubVersionName = (publishVersionProperties["versionName"] as? String) ?: "1.0.0"
val pubAppDescription = (publishVersionProperties["appDescription"] as? String)
    ?: "EvoFit - Seu aplicativo completo para acompanhamento de treinos e evolução física."

android {
    namespace = "com.example.evofit"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.evofit"
        minSdk = 24
        targetSdk = 36
        versionCode = pubVersionCode
        versionName = pubVersionName

        resValue("string", "app_description", pubAppDescription)
        buildConfigField("String", "APP_DESCRIPTION", "\"$pubAppDescription\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions.add("environment")
    productFlavors {
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            resValue("string", "app_name", "EvoFit (HML)")
        }
        create("production") {
            dimension = "environment"
            resValue("string", "app_name", "EvoFit")
        }
    }

    ksp {
        arg("room.generateKotlin", "true")
    }

    signingConfigs {
        create("release") {
            val storeFilePath = keystoreProperties.getProperty("storeFile")
            val storePass = keystoreProperties.getProperty("storePassword")
            val alias = keystoreProperties.getProperty("keyAlias")
            val keyPass = keystoreProperties.getProperty("keyPassword")

            if (!storeFilePath.isNullOrEmpty() && !storePass.isNullOrEmpty() && !alias.isNullOrEmpty()) {
                storeFile = file(storeFilePath)
                storePassword = storePass
                keyAlias = alias
                keyPassword = keyPass.ifEmpty { storePass }
            } else {
                storeFile = file(System.getProperty("user.home") + "/.android/debug.keystore")
                storePassword = "android"
                keyAlias = "androiddebugkey"
                keyPassword = "android"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
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
    buildFeatures {
        compose = true
        resValues = true
        buildConfig = true
    }

    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res",
                "src/main/res-musclegroup",
                "src/main/res-exercises"
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.compose)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)
    implementation(project(":core:monitoring"))
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //page indicators
    implementation(libs.androidx.compose.foundation)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}

tasks.register("testUnit") {
    group = "verification"
    description = "Roda todos os testes unitários do aplicativo (Staging e Production Debug)"
    dependsOn("testStagingDebugUnitTest", "testProductionDebugUnitTest")
}

tasks.matching { it.name == "bundleProductionRelease" }.configureEach {
    doLast {
        val bundleDir = layout.buildDirectory.dir("outputs/bundle/productionRelease").get().asFile
        val defaultBundle = File(bundleDir, "app-production-release.aab")
        val customBundleName = "evofit-prod-${pubVersionCode}-${pubVersionName}-release.aab"
        val customBundle = File(bundleDir, customBundleName)
        if (defaultBundle.exists()) {
            defaultBundle.copyTo(customBundle, overwrite = true)
            println("✅ AAB copiado e gerado em: ${customBundle.absolutePath}")
        }
    }
}