plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    // Internal modules
    implementation(project(":data"))

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