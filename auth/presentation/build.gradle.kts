plugins {
    alias(libs.plugins.runntrak.android.feature.ui)
}

android {
    namespace = "com.skcodes.presentation"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(projects.auth.domain)
}