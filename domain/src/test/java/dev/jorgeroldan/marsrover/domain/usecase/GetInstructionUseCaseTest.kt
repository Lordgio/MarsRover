package dev.jorgeroldan.marsrover.domain.usecase

import arrow.core.Either
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.Failure
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.repository.MarsRoverRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetInstructionUseCaseTest {

    private val repository: MarsRoverRepositoryFake = MarsRoverRepositoryFake(
        instructionResponse = Either.Left(Failure.IOFailure("")),
    )
    private lateinit var useCase: GetInstructionUseCase

    @Before
    fun setup() {
        useCase = GetInstructionUseCase(repository)
    }

    @Test
    fun `on usecase invoke with repository failure returns failure`() = runTest {
        repository.instructionResponse = Either.Left(Failure.IOFailure(""))
        val result = useCase.invoke("test")
        Assert.assertTrue(result is Either.Left)
    }

    @Test
    fun `on usecase invoke with repository success returns success`() = runTest {
        val response = InstructionItem(Coordinates(0, 0), Coordinates(0, 0), RoverDirection.WEST, "", listOf())
        repository.instructionResponse = Either.Right(response)
        val result = useCase.invoke("test")
        Assert.assertTrue(result is Either.Right)
    }

    private class MarsRoverRepositoryFake(
        var instructionResponse: Either<Failure, InstructionItem>,
    ) : MarsRoverRepository {

        override suspend fun getInstructionsList(): Either<Failure, List<Instruction>> = Either.Left(Failure.IOFailure(""))

        override suspend fun getInstruction(instructionPath: String): Either<Failure, InstructionItem> = instructionResponse
    }
}