package com.skcodes.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *>
) {
    commonExtension.apply {
        compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
        defaultConfig.minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        configureKotlin()
        dependencies {
            // Used to make Java backward compatible
            "coreLibraryDesugaring"(libs.findLibrary("desugar.jdk.libs").get())
        }

    }
}


    private fun Project.configureKotlin()
    {
     tasks.withType<KotlinCompile>().configureEach {
         kotlinOptions{
             jvmTarget = JavaVersion.VERSION_1_8.toString()
         }
     }
    }