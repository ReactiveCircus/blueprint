import reactivecircus.blueprint.addResValue
import org.gradle.language.nativeplatform.internal.BuildType

plugins {
    `blueprint-plugin`
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "reactivecircus.blueprint"

    buildFeatures.resValues = true

    defaultConfig {
        applicationId = "reactivecircus.blueprint.demo.rx"
        versionCode = 1
        versionName = "1.0"
        testApplicationId = "reactivecircus.blueprint.demo.rx.test"
        testInstrumentationRunner = "reactivecircus.blueprint.demo.RxScreenTestRunner"
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
            value = "Blueprint RxJava Demo Leaks"
        )
    }
}

dependencies {
    implementation(project(":blueprint-interactor-rx3"))
    implementation(project(":blueprint-async-rx3"))
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

    // rx
    implementation(libs.rxJava3)
    implementation(libs.rxKotlin3)
    implementation(libs.rxAndroid3)

    // Enable LeakCanary for debug builds
    debugImplementation(libs.leakcanary.android)
    // Fix SDK leaks
    implementation(libs.leakcanary.plumber)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.androidx.arch.coreTesting)

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
