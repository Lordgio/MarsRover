package dev.jorgeroldan.marsrover.ui.di

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class UiModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `UiModule di check`() {
        UiModule.module.verify(extraTypes = listOf(
            android.content.Context::class,
            androidx.lifecycle.SavedStateHandle::class,
            dev.jorgeroldan.marsrover.domain.usecase.GetInstructionsListUseCaseImpl::class,
            dev.jorgeroldan.marsrover.domain.usecase.GetInstructionUseCase::class,
        ))
    }
}