package com.skcodes.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposePlugin:Plugin<Project>{
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("runntrak.android.application")
            val extention = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extention)
        }
    }

}