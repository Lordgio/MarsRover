plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    // Internal modules
    implementation(project(":domain"))

    // DI
    implementation(libs.koin.core)

    // Kotlin
    implementation(libs.coroutines.core)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Functional programming
    implementation(libs.arrow.core)

    // HTTP handling
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.serialization)
    implementation(libs.retrofit.adapter.either)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.koin.test)
}