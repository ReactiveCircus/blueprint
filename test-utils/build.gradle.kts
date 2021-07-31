plugins {
    `blueprint-plugin`
    kotlin("jvm")
}

dependencies {
    implementation(libs.junit)
    implementation(libs.truth)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.test)
}
