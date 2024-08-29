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
    }
}