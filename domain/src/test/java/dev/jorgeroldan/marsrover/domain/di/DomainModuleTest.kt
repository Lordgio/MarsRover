package dev.jorgeroldan.marsrover.domain.di

import org.junit.Test
import org.koin.test.verify.verify

class DomainModuleTest {

    @Test
    fun `DomainModule di check`() {
        DomainModule.module.verify(
            extraTypes = listOf(dev.jorgeroldan.marsrover.domain.repository.MarsRoverRepository::class)
        )
    }
}