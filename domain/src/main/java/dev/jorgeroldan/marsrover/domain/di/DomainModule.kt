package dev.jorgeroldan.marsrover.domain.di

import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionUseCase
import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionUseCaseImpl
import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionsListUseCase
import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionsListUseCaseImpl
import org.koin.dsl.module

object DomainModule {

    val module = module {
        factory<GetInstructionsListUseCase> { GetInstructionsListUseCaseImpl(get()) }
        factory<GetInstructionUseCase> { GetInstructionUseCaseImpl(get()) }
    }
}