plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `kotlin-dsl`
}

group = "com.skcodes.runntrak.buildlogic"

dependencies{
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin{
    plugins{
        register(
           "androidApplication"
        ){
            id= "runntrak.android.application"
            implementationClass = "com.skcodes.convention.AndroidApplicationConventionPlugin"
        }

        register(
            "androidComposeApplication"
        ){
            id = "runntrak.android.application.compose"
            implementationClass = "com.skcodes.convention.AndroidApplicationComposePlugin"
        }

        register(
            "androidLibraryApplication"
        ){
            id = "runntrak.android.library"
            implementationClass = "com.skcodes.convention.AndroidLibraryConventionPlugin"
        }

        register(
            "androidLibraryComposeApplication"
        ){
            id = "runntrak.android.library.compose"
            implementationClass = "com.skcodes.convention.AndroidLibraryComposeConventionPlugin"
        }

        register(
            "androidFeatureUi"
        ){
            id = "runntrak.android.feature.ui"
            implementationClass = "com.skcodes.convention.AndroidFeatureUiCoventionPlugin"
        }

        register(
            "androidRoom"
        ){
            id = "runntrak.android.room"
            implementationClass = "com.skcodes.convention.AndroidRoomConventionPlugin"
        }

        register(
            "androidJvm"
        ){
            id = "runntrak.android.jvm"
            implementationClass = "com.skcodes.convention.JvmLibraryConventionPlugin"
        }

        register(
            "androidJvmKtor"
        ){
            id = "runntrak.android.jvm.ktor"
            implementationClass = "com.skcodes.convention.JvmKtorConventionPlugin"
        }

        register(
            "androidDynamicFeature"
        ){
            id = "runntrak.android.dynamic.feature"
            implementationClass = "com.skcodes.convention.AndroidDynamicFeatureConventionPlugin"
        }


    }
}