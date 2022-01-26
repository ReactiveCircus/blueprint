plugins {
    `blueprint-plugin`
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
}

android {
    buildFeatures {
        androidResources = true
    }
    lint {
        disable.add("IconDuplicates")
        disable.add("MissingClass")
    }
}

dependencies {
    implementation(project(":blueprint-ui"))

    // AndroidX
    implementation(libs.androidx.coordinatorLayout)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.lifecycle.viewModel)

    // Material Components
    implementation(libs.material)

    // timber
    implementation(libs.timber)

    // Unit tests
    testImplementation(project(":test-utils"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
}
