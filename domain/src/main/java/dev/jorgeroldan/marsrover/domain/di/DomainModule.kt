package dev.jorgeroldan.marsrover.domain.di

import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionUseCase
import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionsListUseCase
import org.koin.dsl.module

object DomainModule {

    val module = module {
        factory<GetInstructionsListUseCase> { GetInstructionsListUseCase(get()) }
        factory<GetInstructionUseCase> { GetInstructionUseCase(get()) }
    }
}