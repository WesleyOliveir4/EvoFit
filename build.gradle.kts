plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

tasks.register("testUnit") {
    group = "verification"
    description = "Roda todos os testes unitários do projeto"
    dependsOn(":app:testUnit")
}