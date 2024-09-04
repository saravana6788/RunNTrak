plugins {
    alias(libs.plugins.runntrak.android.library.application)
    alias(libs.plugins.runntrak.android.jvm.ktor)
}

android {
    namespace = "com.skcodes.auth.data"
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