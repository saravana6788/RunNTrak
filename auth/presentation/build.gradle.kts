plugins {
    alias(libs.plugins.runntrak.android.feature.ui)
}

android {
    namespace = "com.skcodes.presentation"
}

dependencies {


    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
}