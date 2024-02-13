package dev.jorgeroldan.marsrover.data.di

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class DataModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `DataModule di check`() {
        DataModule.module.verify()
    }
}