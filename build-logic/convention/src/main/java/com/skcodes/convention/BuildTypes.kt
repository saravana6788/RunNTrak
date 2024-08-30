package com.skcodes.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*,*,*,*,*>,
    extensionType: ExtensionType,
){
    commonExtension.run{
        buildFeatures{
            buildConfig = true
        }
        val api_key = gradleLocalProperties(rootDir).getProperty("api_key")
                when(extensionType){
                    ExtensionType.APPLICATION -> {
                        extensions.configure<ApplicationExtension> {
                            buildTypes {
                                debug {
                                    configureDebugBuildType(api_key)
                                }

                                release {
                                    configureReleaseBuildType(api_key,commonExtension)
                                }
                            }
                        }
                    }
                    ExtensionType.LIBRARY -> {
                        extensions.configure<LibraryExtension>{

                    }
                }


        }
    }
}


private fun BuildType.configureDebugBuildType(api_key: String) {
    buildConfigField(
        type = "String",
        name = "API_KEY",
        value = "\"$api_key\""
    )

    buildConfigField(
        type = "String",
        name = "BASE_URL",
        value = "\"https://runique.pl-coding.com:8080\""
    )
}


private fun BuildType.configureReleaseBuildType(api_key: String,
                                                commonExtension: CommonExtension<*, *, *, *, *>) {
    buildConfigField(
        type = "String",
        name = "API_KEY",
        value = "\"$api_key\""
    )

    buildConfigField(
        type = "String",
        name = "BASE_URL",
        value = "\"https://runique.pl-coding.com:8080\""
    )
    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )

}