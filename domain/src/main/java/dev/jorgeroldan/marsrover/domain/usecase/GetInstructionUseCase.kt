package dev.jorgeroldan.marsrover.domain.usecase

import arrow.core.Either
import dev.jorgeroldan.marsrover.domain.model.Failure
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.repository.MarsRoverRepository

class GetInstructionUseCase(private val repository: MarsRoverRepository) {

    suspend operator fun invoke(urlPath: String) : Either<Failure, InstructionItem> {
        return repository.getInstruction(urlPath)
    }
}