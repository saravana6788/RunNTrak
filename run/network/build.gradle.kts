plugins {
    alias(libs.plugins.runntrak.android.library.application)
    alias(libs.plugins.runntrak.android.jvm.ktor)
}

android {
    namespace = "com.skcodes.run.network"

}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(libs.bundles.koin)

}