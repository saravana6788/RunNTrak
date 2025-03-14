plugins {
    alias(libs.plugins.runntrak.android.library.application)
}

android {
    namespace = "com.skcodes.run.location"

}

dependencies {



    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.android.gms.play.services.location)
    implementation(projects.core.domain)
    implementation(projects.run.domain)
    implementation(libs.bundles.koin)
}