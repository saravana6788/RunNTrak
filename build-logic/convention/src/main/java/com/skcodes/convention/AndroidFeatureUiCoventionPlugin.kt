package com.skcodes.convention

import addComposeUiDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiCoventionPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run{
                apply("runntrak.android.library.compose")
            }
            dependencies {
                addComposeUiDependencies(target)
            }
        }
    }

}