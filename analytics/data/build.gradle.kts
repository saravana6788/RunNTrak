plugins {
    alias(libs.plugins.runntrak.android.library.application)
    alias(libs.plugins.runntrak.android.room)
}

android {
    namespace = "com.skcodes.analytics.data"
}

dependencies {

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.analytics.domain)
}