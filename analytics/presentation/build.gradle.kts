plugins {
    alias(libs.plugins.runntrak.android.feature.ui)

}

android {
    namespace = "com.skcodes.analytics.presentation"
}

dependencies {

    implementation(projects.analytics.domain)
}