package dev.jorgeroldan.marsrover.domain.usecase

import arrow.core.Either
import dev.jorgeroldan.marsrover.domain.model.Failure
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.repository.MarsRoverRepository

interface GetInstructionsListUseCase {
    suspend operator fun invoke() : Either<Failure, List<Instruction>>
}

class GetInstructionsListUseCaseImpl(
    private val repository: MarsRoverRepository
) : GetInstructionsListUseCase {

    override suspend operator fun invoke() : Either<Failure, List<Instruction>> {
        return repository.getInstructionsList()
    }
}