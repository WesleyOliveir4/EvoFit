plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

tasks.register("testUnit") {
    group = "verification"
    description = "Roda todos os testes unitários do projeto"
    dependsOn(":app:testUnit")
}

tasks.register("GenerateApkRelease") {
    group = "publish"
    description = "Gera o APK assinado de produção (Release)"
    dependsOn(":app:GenerateApkRelease")
}

tasks.register("GenerateApkStaging") {
    group = "publish"
    description = "Gera o APK assinado de homologação (Staging)"
    dependsOn(":app:GenerateApkStaging")
}

tasks.register("GenerateAabRelease") {
    group = "publish"
    description = "Gera o AAB assinado de produção (Release)"
    dependsOn(":app:GenerateAabRelease")
}

tasks.register("GenerateAabStaging") {
    group = "publish"
    description = "Gera o AAB assinado de homologação (Staging)"
    dependsOn(":app:GenerateAabStaging")
}