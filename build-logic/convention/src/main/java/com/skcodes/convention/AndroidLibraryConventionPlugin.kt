package com.skcodes.convention


import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

// This creates the plugin for Kotlin library modules eg:Core:data
class AndroidLibraryConventionPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension>{
                configureBuildTypes(
                    this,ExtensionType.LIBRARY
                )

                configureKotlinAndroid(this)

                defaultConfig{
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }


            dependencies {
                "testImplementation"(kotlin("test"))
            }
        }
    }
}