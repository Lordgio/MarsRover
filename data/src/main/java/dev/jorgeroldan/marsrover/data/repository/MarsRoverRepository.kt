package dev.jorgeroldan.marsrover.data.repository

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import arrow.retrofit.adapter.either.networkhandling.HttpError
import arrow.retrofit.adapter.either.networkhandling.IOError
import arrow.retrofit.adapter.either.networkhandling.UnexpectedCallError
import dev.jorgeroldan.marsrover.data.remote.MarsRoverApi
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.Failure
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.repository.MarsRoverRepository

class MarsRoverRepositoryImpl(
    private val remote: MarsRoverApi
) : MarsRoverRepository {

    override suspend fun getInstructionsList(): Either<Failure, List<Instruction>> {

        return remote.getInstructionsList()
            .map { it.map { inst -> Instruction(inst.name, inst.urlPath) } }
            .mapLeft { error -> error.mapError() }
    }

    override suspend fun getInstruction(instructionPath: String): Either<Failure, InstructionItem> {

        return remote.getInstruction(instructionPath)
            .map { item -> InstructionItem(Coordinates(1, 1), Coordinates(1, 2), RoverDirection.EAST, "") }
            .mapLeft { error -> error.mapError() }
    }

    private fun CallError.mapError(): Failure {
        return when (this) {
            is HttpError -> Failure.HttpFailure(this.code.toString(), this.message)
            is IOError -> Failure.IOFailure(this.cause.message ?: "")
            is UnexpectedCallError -> Failure.UnexpectedFailure(this.cause.message ?: "")
        }
    }
}