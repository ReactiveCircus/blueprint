plugins {
    `kotlin-dsl`
}

@Suppress("UnstableApiUsage")
dependencies {
    // TODO: remove once https://github.com/gradle/gradle/issues/15383#issuecomment-779893192 is fixed
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.dokka)
    implementation(libs.plugin.agp)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.binaryCompatibilityValidator)
    implementation(libs.plugin.mavenPublish)
}

gradlePlugin {
    plugins {
        register("blueprint") {
            id = "blueprint-plugin"
            implementationClass = "reactivecircus.blueprint.BlueprintPlugin"
        }
    }
}
