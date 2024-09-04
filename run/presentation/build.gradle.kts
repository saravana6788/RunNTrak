plugins {
    alias(libs.plugins.runntrak.android.feature.ui)
}

android {
    namespace = "com.skcodes.run.presentation"
}

dependencies {

    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)
    implementation(projects.run.domain)
    implementation(projects.core.domain)
}