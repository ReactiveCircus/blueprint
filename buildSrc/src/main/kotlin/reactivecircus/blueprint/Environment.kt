package reactivecircus.blueprint

import org.gradle.api.Project

@Suppress("UnstableApiUsage")
val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").orNull == "true"
