plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    // DI
    implementation(libs.koin.core)

    // Kotlin
    implementation(libs.coroutines.core)

    // Serialization
    implementation(libs.kotlin.serialization.json)

    // Functional programming
    implementation(libs.arrow.core)

    // Testing
    testImplementation(libs.mockk.agent)
    testImplementation(libs.coroutines.test)
}