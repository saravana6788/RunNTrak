package com.skcodes.convention

import androidx.room.gradle.RoomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        target.run {
            pluginManager.run{
                apply("androidx.room")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<RoomExtension>{
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                "implementation"(target.libs.findLibrary("room.runtime").get())
                "implementation"(target.libs.findLibrary("room.ktx").get())
                "ksp"(target.libs.findLibrary("room.compiler").get())
            }
        }
    }
}