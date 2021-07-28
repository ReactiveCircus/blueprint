plugins {
    `blueprint-plugin`
    id("com.android.library")
    kotlin("android")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}

blueprint {
    enableExplicitApi.set(true)
}

android {
    defaultConfig {
        testApplicationId = "reactivecircus.blueprint.testing.testapp"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // AndroidX
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerView)

    // Material components
    implementation(libs.material)

    implementation(libs.truth)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.espresso.contrib)
    implementation(libs.androidx.espresso.intents)

    // Android tests
    debugImplementation(libs.androidx.fragment.testing) {
        exclude(group = "androidx.test")
    }
    androidTestImplementation(libs.androidx.test.monitor)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
