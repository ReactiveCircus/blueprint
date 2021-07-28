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

dependencies {
    // AndroidX
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.core)
}
