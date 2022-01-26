import reactivecircus.blueprint.addResValue
import org.gradle.language.nativeplatform.internal.BuildType

plugins {
    `blueprint-plugin`
    id("com.android.application")
    kotlin("android")
}

android {
    buildFeatures.resValues = true

    defaultConfig {
        applicationId = "reactivecircus.blueprint.demo.coroutines"
        versionCode = 1
        versionName = "1.0"
        testApplicationId = "reactivecircus.blueprint.demo.coroutines.test"
        testInstrumentationRunner = "reactivecircus.blueprint.demo.CoroutinesScreenTestRunner"
    }

    lint {
        disable.add("GoogleAppIndexingWarning")
    }

    buildTypes {
        named(BuildType.RELEASE.name) {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("shrinker-rules.pro")
        }
    }
}

androidComponents {
    onVariants(selector().withBuildType(BuildType.DEBUG.name)) {
        it.addResValue(
            key = "leak_canary_display_activity_label",
            type = "string",
            value = "Blueprint Coroutines Demo Leaks"
        )
    }
}

dependencies {
    implementation(project(":blueprint-interactor-coroutines"))
    implementation(project(":blueprint-async-coroutines"))
    implementation(project(":blueprint-ui"))
    implementation(project(":demo-common"))

    // AndroidX
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coordinatorLayout)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.lifecycle.commonJava8)

    // Material Components
    implementation(libs.material)

    // timber
    implementation(libs.timber)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Enable LeakCanary for debug builds
    debugImplementation(libs.leakcanary.android)
    // Fix SDK leaks
    implementation(libs.leakcanary.plumber)

    // Unit tests
    testImplementation(project(":test-utils"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android tests
    androidTestImplementation(project(":blueprint-testing-robot"))
    androidTestImplementation(project(":demo-testing-common"))
    debugImplementation(libs.androidx.fragment.testing) {
        exclude(group = "androidx.test")
    }
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.monitor)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.intents)
}
