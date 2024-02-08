package dev.jorgeroldan.marsrover.domain.repository

import arrow.core.Either
import dev.jorgeroldan.marsrover.domain.model.Failure
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.model.InstructionItem

interface MarsRoverRepository {

    suspend fun getInstructionsList(): Either<Failure, List<Instruction>>

    suspend fun getInstruction(instructionPath: String): Either<Failure, InstructionItem>
}