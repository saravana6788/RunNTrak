plugins {
    alias(libs.plugins.runntrak.android.library.application)
    alias(libs.plugins.runntrak.android.room)
}

android {
    namespace = "com.skcodes.core.database"
}

dependencies {

    implementation(libs.org.mongodb.bson)
    implementation(libs.bundles.koin)
    implementation(projects.core.domain)

}