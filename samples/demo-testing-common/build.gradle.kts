plugins {
    `blueprint-plugin`
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "reactivecircus.blueprint"
}

dependencies {
    implementation(project(":demo-common"))
    implementation(project(":blueprint-testing-robot"))

    // Espresso
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.espresso.contrib)
    implementation(libs.androidx.espresso.intents)
}
