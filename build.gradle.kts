buildscript {
    apply(from = "buildSrc/dependencies.gradle")
    val versions: Map<Any, Any> by extra

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${versions.getValue("androidGradlePlugin")}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.getValue("kotlin")}")
        classpath("org.jetbrains.kotlinx:binary-compatibility-validator:${versions.getValue("binaryCompatibilityValidator")}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${versions.getValue("detekt")}")
        classpath("com.vanniktech:gradle-maven-publish-plugin:${versions.getValue("mavenPublishPlugin")}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${versions.getValue("dokka")}")
    }
}

plugins {
    `blueprint-plugin`
}

apply(plugin = "binary-compatibility-validator")

configure<kotlinx.validation.ApiValidationExtension> {
    ignoredProjects.addAll(
        listOf("demo-common", "demo-coroutines", "demo-rx", "demo-testing-common")
    )
}

subprojects {
    apply(from = "${project.rootDir}/gradle/detekt.gradle")
}
