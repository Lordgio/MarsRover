plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(11)
}

dependencies {

    // Kotlin
    implementation(libs.coroutines.core)

    // DI
    implementation(libs.koin.core)

    // Functional programming
    implementation(libs.arrow.core)

    // Testing
    testImplementation(libs.mockk.agent)
    testImplementation(libs.coroutines.test)
}