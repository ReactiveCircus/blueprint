package reactivecircus.blueprint

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import kotlinx.validation.ApiValidationExtension
import kotlinx.validation.BinaryCompatibilityValidatorPlugin

/**
 * Apply and configure the [BinaryCompatibilityValidatorPlugin] for the [Project].
 */
internal fun Project.configureBinaryCompatibilityValidation() {
    pluginManager.apply(BinaryCompatibilityValidatorPlugin::class.java)
    plugins.withType<BinaryCompatibilityValidatorPlugin> {
        extensions.configure<ApiValidationExtension> {
            ignoredProjects.addAll(IGNORED_PROJECTS)
        }
    }
}

private val IGNORED_PROJECTS = listOf(
    "demo-common",
    "demo-coroutines",
    "demo-rx",
    "demo-testing-common",
    "test-utils",
)
