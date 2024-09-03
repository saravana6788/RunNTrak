plugins {
    alias(libs.plugins.runntrak.android.library.application.compose)
}

android {
    namespace = "com.skcodes.presentation.designsystem"

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)

}