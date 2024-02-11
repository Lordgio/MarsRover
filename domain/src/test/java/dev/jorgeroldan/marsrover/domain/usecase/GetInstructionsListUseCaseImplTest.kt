package dev.jorgeroldan.marsrover.domain.usecase

import arrow.core.Either
import dev.jorgeroldan.marsrover.domain.model.Failure
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.repository.MarsRoverRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetInstructionsListUseCaseImplTest {

    private val repository: MarsRoverRepositoryFake = MarsRoverRepositoryFake(
        instructionsListResponse = Either.Left(Failure.IOFailure("")),
    )
    private lateinit var useCase: GetInstructionsListUseCaseImpl

    @Before
    fun setup() {
        useCase = GetInstructionsListUseCaseImpl(repository)
    }

    @Test
    fun `on usecase invoke with repository failure returns failure`() = runTest {
        repository.instructionsListResponse = Either.Left(Failure.IOFailure(""))
        val result = useCase.invoke()
        Assert.assertTrue(result is Either.Left)
    }

    @Test
    fun `on usecase invoke with repository success returns success`() = runTest {
        repository.instructionsListResponse = Either.Right(listOf())
        val result = useCase.invoke()
        Assert.assertTrue(result is Either.Right)
    }

    private class MarsRoverRepositoryFake(
        var instructionsListResponse: Either<Failure, List<Instruction>>,
    ) : MarsRoverRepository {

        override suspend fun getInstructionsList(): Either<Failure, List<Instruction>> = instructionsListResponse

        override suspend fun getInstruction(instructionPath: String): Either<Failure, InstructionItem> = Either.Left(Failure.IOFailure(""))
    }
}