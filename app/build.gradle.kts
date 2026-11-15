import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

fun getPublishVersionProperties(): Properties {
    val file = rootProject.file("publish-version.properties")
    val props = Properties()
    if (file.exists()) {
        file.inputStream().use { props.load(it) }
    }
    return props
}

fun getPublishVersionCode(): Int {
    return (getPublishVersionProperties()["versionCode"] as? String)?.toIntOrNull() ?: 1
}

fun getPublishVersionName(): String {
    return (getPublishVersionProperties()["versionName"] as? String) ?: "1.0.1"
}

fun getPublishAppDescription(): String {
    return (getPublishVersionProperties()["appDescription"] as? String)
        ?: "EvoFit - Seu aplicativo completo para acompanhamento de treinos e evolução física."
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.guaracode.evofit"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.guaracode.evofit"
        minSdk = 24
        targetSdk = 36
        versionCode = getPublishVersionCode()
        versionName = getPublishVersionName()

        resValue("string", "app_description", getPublishAppDescription())
        buildConfigField("String", "APP_DESCRIPTION", "\"${getPublishAppDescription()}\"")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    implementation(project(":shared"))
    implementation(project(":core:monitoring"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation("org.jetbrains.compose.material:material-icons-extended:1.7.0")
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.compose)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
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

tasks.register("generateReleaseAab") {
    group = "publishing"
    description = "Gera o AAB assinado de produção para a Play Store e envia para a pasta 'outputs' na raiz do projeto"
    dependsOn("bundleProductionRelease")

    doLast {
        val outputDir = rootProject.file("outputs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val customBundleName = "evofit-prod-${getPublishVersionCode()}-${getPublishVersionName()}-release.aab"
        val destinationFile = File(outputDir, customBundleName)
        val bundleDir = layout.buildDirectory.dir("outputs/bundle/productionRelease").get().asFile
        val customBundle = File(bundleDir, customBundleName)
        val defaultBundle = File(bundleDir, "app-production-release.aab")
        val sourceBundle = if (customBundle.exists()) customBundle else defaultBundle

        if (sourceBundle.exists()) {
            sourceBundle.copyTo(destinationFile, overwrite = true)
            println("🚀 AAB assinado gerado com sucesso em: ${destinationFile.absolutePath}")
        }
    }
}

tasks.matching { it.name == "bundleProductionRelease" }.configureEach {
    doLast {
        val bundleDir = layout.buildDirectory.dir("outputs/bundle/productionRelease").get().asFile
        val defaultBundle = File(bundleDir, "app-production-release.aab")
        val customBundleName = "evofit-prod-${getPublishVersionCode()}-${getPublishVersionName()}-release.aab"
        val customBundle = File(bundleDir, customBundleName)
        if (defaultBundle.exists()) {
            defaultBundle.copyTo(customBundle, overwrite = true)
            println("✅ AAB copiado e gerado em: ${customBundle.absolutePath}")
        }

        val outputDir = rootProject.file("outputs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val destinationFile = File(outputDir, customBundleName)
        val sourceBundle = if (customBundle.exists()) customBundle else defaultBundle
        if (sourceBundle.exists()) {
            sourceBundle.copyTo(destinationFile, overwrite = true)
            println("🚀 AAB disponibilizado na pasta outputs: ${destinationFile.absolutePath}")
        }
    }
}

tasks.register("GenerateApkRelease") {
    group = "publishing"
    description = "Gera o APK assinado de produção (Release) e copia para a pasta 'outputs' na raiz do projeto"
    dependsOn("assembleProductionRelease")

    doLast {
        val outputDir = rootProject.file("outputs")
        if (!outputDir.exists()) outputDir.mkdirs()
        val customName = "evofit-prod-${getPublishVersionCode()}-${getPublishVersionName()}-release.apk"
        val destinationFile = File(outputDir, customName)
        val apkDir = layout.buildDirectory.dir("outputs/apk/production/release").get().asFile
        val defaultApk = File(apkDir, "app-production-release.apk")
        if (defaultApk.exists()) {
            defaultApk.copyTo(destinationFile, overwrite = true)
            println("🚀 APK de produção gerado em: ${destinationFile.absolutePath}")
        }
    }
}

tasks.register("GenerateApkStaging") {
    group = "publishing"
    description = "Gera o APK assinado de homologação (Staging) e copia para a pasta 'outputs' na raiz do projeto"
    dependsOn("assembleStagingRelease")

    doLast {
        val outputDir = rootProject.file("outputs")
        if (!outputDir.exists()) outputDir.mkdirs()
        val customName = "evofit-staging-${getPublishVersionCode()}-${getPublishVersionName()}-release.apk"
        val destinationFile = File(outputDir, customName)
        val apkDir = layout.buildDirectory.dir("outputs/apk/staging/release").get().asFile
        val defaultApk = File(apkDir, "app-staging-release.apk")
        if (defaultApk.exists()) {
            defaultApk.copyTo(destinationFile, overwrite = true)
            println("🚀 APK de staging gerado em: ${destinationFile.absolutePath}")
        }
    }
}

tasks.register("GenerateAabRelease") {
    group = "publishing"
    description = "Gera o AAB assinado de produção (Release) e copia para a pasta 'outputs' na raiz do projeto"
    dependsOn("bundleProductionRelease")

    doLast {
        val outputDir = rootProject.file("outputs")
        if (!outputDir.exists()) outputDir.mkdirs()
        val customName = "evofit-prod-${getPublishVersionCode()}-${getPublishVersionName()}-release.aab"
        val destinationFile = File(outputDir, customName)
        val bundleDir = layout.buildDirectory.dir("outputs/bundle/productionRelease").get().asFile
        val defaultBundle = File(bundleDir, "app-production-release.aab")
        if (defaultBundle.exists()) {
            defaultBundle.copyTo(destinationFile, overwrite = true)
            println("🚀 AAB de produção gerado em: ${destinationFile.absolutePath}")
        }
    }
}

tasks.register("GenerateAabStaging") {
    group = "publishing"
    description = "Gera o AAB assinado de homologação (Staging) e copia para a pasta 'outputs' na raiz do projeto"
    dependsOn("bundleStagingRelease")

    doLast {
        val outputDir = rootProject.file("outputs")
        if (!outputDir.exists()) outputDir.mkdirs()
        val customName = "evofit-staging-${getPublishVersionCode()}-${getPublishVersionName()}-release.aab"
        val destinationFile = File(outputDir, customName)
        val bundleDir = layout.buildDirectory.dir("outputs/bundle/stagingRelease").get().asFile
        val defaultBundle = File(bundleDir, "app-staging-release.aab")
        if (defaultBundle.exists()) {
            defaultBundle.copyTo(destinationFile, overwrite = true)
            println("🚀 AAB de staging gerado em: ${destinationFile.absolutePath}")
        }
    }
}
