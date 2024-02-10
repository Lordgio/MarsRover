package dev.jorgeroldan.marsrover.data.di

import org.junit.Test
import org.koin.test.verify.verify

class DataModuleTest {

    @Test
    fun `DataModule di check`() {
        DataModule.module.verify()
    }
}