@file:Suppress("unused")

package reactivecircus.blueprint

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByType

/**
 * A plugin that provides baseline gradle configurations for all projects, including:
 * - root project
 * - Android Application projects
 * - Android Library projects
 * - Kotlin JVM projects
 * - Java JVM projects
 *
 * Apply this plugin to the build.gradle.kts file in all projects:
 * ```
 * plugins {
 *     id 'blueprint-plugin'
 * }
 * ```
 */
@ExperimentalStdlibApi
class BlueprintPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val flowBindingExtension = project.extensions.create("blueprint", BlueprintExtension::class.java)

        project.configureForAllProjects(flowBindingExtension.enableExplicitApi)

        // apply configurations specific to root project
        if (project.isRoot) {
            project.configureRootProject()
        }

        // apply baseline configurations based on plugins applied
        project.plugins.all {
            when (this) {
                is JavaPlugin,
                is JavaLibraryPlugin -> {
                    project.extensions.getByType<JavaPluginExtension>().apply {
                        sourceCompatibility = JavaVersion.VERSION_11
                        targetCompatibility = JavaVersion.VERSION_11
                    }
                }
                is LibraryPlugin -> {
                    project.configureAndroidLibrary()
                }
                is AppPlugin -> {
                    project.configureAndroidApplication()
                }
            }
        }
    }
}

val Project.isRoot get() = this == this.rootProject
