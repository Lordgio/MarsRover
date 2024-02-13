package dev.jorgeroldan.marsrover.data.repository

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import arrow.retrofit.adapter.either.networkhandling.HttpError
import arrow.retrofit.adapter.either.networkhandling.IOError
import arrow.retrofit.adapter.either.networkhandling.UnexpectedCallError
import dev.jorgeroldan.marsrover.data.model.CoordinatesApi
import dev.jorgeroldan.marsrover.data.model.InstructionApi
import dev.jorgeroldan.marsrover.data.model.InstructionItemApi
import dev.jorgeroldan.marsrover.data.remote.MarsRoverApi
import dev.jorgeroldan.marsrover.domain.model.Coordinates as DomainCoordinates
import dev.jorgeroldan.marsrover.domain.model.Failure
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MarsRoverRepositoryImplTest {

    private val marsRoverApi = MarsRoverApiFake(
        instructionsListResponse = Either.Left(UnexpectedCallError(Throwable())),
        instructionResponse = Either.Left(UnexpectedCallError(Throwable())),
    )
    private lateinit var repository: MarsRoverRepositoryImpl

    @Before
    fun setup() {
        repository = MarsRoverRepositoryImpl(marsRoverApi)
    }

    @Test
    fun `getInstructionsList with api unexpected error returns UnexpectedFailure`() = runTest {
        marsRoverApi.instructionsListResponse = Either.Left(UnexpectedCallError(Throwable()))

        val result = repository.getInstructionsList()
        Assert.assertTrue(result is Either.Left)
        Assert.assertTrue(result.leftOrNull() is Failure.UnexpectedFailure)
    }

    @Test
    fun `getInstructionsList with api io error returns IOFailure`() = runTest {
        marsRoverApi.instructionsListResponse = Either.Left(IOError(IOException()))

        val result = repository.getInstructionsList()
        Assert.assertTrue(result is Either.Left)
        Assert.assertTrue(result.leftOrNull() is Failure.IOFailure)
    }

    @Test
    fun `getInstructionsList with api http error returns HTTPFailure`() = runTest {
        marsRoverApi.instructionsListResponse = Either.Left(HttpError(0, "", ""))

        val result = repository.getInstructionsList()
        Assert.assertTrue(result is Either.Left)
        Assert.assertTrue(result.leftOrNull() is Failure.HttpFailure)
    }

    @Test
    fun `getInstructionsList with api success returns mapped values`() = runTest {
        val value = InstructionApi("test", "testUrl")
        marsRoverApi.instructionsListResponse = Either.Right(listOf(value))

        val result = repository.getInstructionsList()
        Assert.assertTrue(result is Either.Right)
        Assert.assertEquals(1, result.getOrNull()!!.size)
        Assert.assertEquals("test", result.getOrNull()!!.first().name)
        Assert.assertEquals("testUrl", result.getOrNull()!!.first().urlPath)
    }

    @Test
    fun `getInstruction with api unexpected error returns UnexpectedFailure`() = runTest {
        marsRoverApi.instructionResponse = Either.Left(UnexpectedCallError(Throwable()))

        val result = repository.getInstruction("testUrl")
        Assert.assertTrue(result is Either.Left)
        Assert.assertTrue(result.leftOrNull() is Failure.UnexpectedFailure)
    }

    @Test
    fun `getInstruction with api io error returns IOFailure`() = runTest {
        marsRoverApi.instructionResponse = Either.Left(IOError(IOException()))

        val result = repository.getInstruction("testUrl")
        Assert.assertTrue(result is Either.Left)
        Assert.assertTrue(result.leftOrNull() is Failure.IOFailure)
    }

    @Test
    fun `getInstruction with api http error returns HTTPFailure`() = runTest {
        marsRoverApi.instructionResponse = Either.Left(HttpError(0, "", ""))

        val result = repository.getInstruction("testUrl")
        Assert.assertTrue(result is Either.Left)
        Assert.assertTrue(result.leftOrNull() is Failure.HttpFailure)
    }

    @Test
    fun `getInstruction with api success returns mapped values`() = runTest {
        val value = InstructionItemApi(CoordinatesApi(0,0), CoordinatesApi(0,0), "A", "B")
        marsRoverApi.instructionResponse = Either.Right(value)

        val result = repository.getInstruction("testUrl")
        Assert.assertTrue(result is Either.Right)
        Assert.assertEquals(RoverDirection.NORTH, result.getOrNull()!!.roverDirection)
        Assert.assertEquals("B", result.getOrNull()!!.encodedMovements)
        Assert.assertEquals(emptyList<RoverMovement>(), result.getOrNull()!!.movements)
        Assert.assertEquals(DomainCoordinates(0,0), result.getOrNull()!!.topRightCorner)
    }
}

private class MarsRoverApiFake(
    var instructionsListResponse: Either<CallError, List<InstructionApi>>,
    var instructionResponse: Either<CallError, InstructionItemApi>
) : MarsRoverApi {
    override suspend fun getInstructionsList(): Either<CallError, List<InstructionApi>> = instructionsListResponse

    override suspend fun getInstruction(instructionName: String): Either<CallError, InstructionItemApi> = instructionResponse
}