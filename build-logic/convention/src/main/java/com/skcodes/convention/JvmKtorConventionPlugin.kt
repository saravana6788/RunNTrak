package com.skcodes.convention


import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

// This creates the plugin for Kotlin library modules eg:Core:data
class JvmKtorConventionPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.serialization")

            }
            dependencies {
                "implementation"(libs.findBundle("ktor").get())
            }

        }
    }
}