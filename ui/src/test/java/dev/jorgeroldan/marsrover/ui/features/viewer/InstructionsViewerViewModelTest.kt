package dev.jorgeroldan.marsrover.ui.features.viewer

import androidx.lifecycle.SavedStateHandle
import arrow.core.Either
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.Failure
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.model.MovementStep
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement
import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionUseCase
import dev.jorgeroldan.marsrover.ui.util.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class InstructionsViewerViewModelTest {

    private val savedStateHandle = SavedStateHandle()
    private val useCase = GetInstructionUseCaseFake()
    private val resourcesProvider = ResourcesProviderFake()
    private lateinit var viewModel: InstructionsViewerViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `init viewmodel with usecase error configs proper states`() = runTest {
        useCase.useCaseResponse = Either.Left(Failure.UnexpectedFailure(""))
        viewModel = InstructionsViewerViewModel(savedStateHandle, resourcesProvider, useCase)
        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            var expectedState: InstructionsViewerViewModel.InstructionsViewerState = InstructionsViewerViewModel.InstructionsViewerState.Idle
            viewModel.state.collect { state ->
                when(state) {
                    InstructionsViewerViewModel.InstructionsViewerState.Idle -> {
                        Assert.assertEquals(expectedState, state)
                        expectedState = InstructionsViewerViewModel.InstructionsViewerState.Loading
                    }
                    InstructionsViewerViewModel.InstructionsViewerState.Loading -> {
                        Assert.assertEquals(expectedState, state)
                        expectedState = InstructionsViewerViewModel.InstructionsViewerState.Error
                    }
                    is InstructionsViewerViewModel.InstructionsViewerState.Data -> {
                        Assert.assertEquals(expectedState, state)
                    }
                    InstructionsViewerViewModel.InstructionsViewerState.Error -> {
                        Assert.assertEquals(expectedState, state)
                        this.cancel()
                    }
                }
            }
        }
        viewModel.initViewModel("test")
        collector.join()
    }

    @Test
    fun `init viewmodel with instruction error configs proper states`() = runTest {
        val response = InstructionItem(Coordinates(5, 5), Coordinates(0,0), RoverDirection.WEST, "M", listOf(RoverMovement.MOVE))
        useCase.useCaseResponse = Either.Right(response)
        viewModel = InstructionsViewerViewModel(savedStateHandle, resourcesProvider, useCase)
        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            var expectedState: InstructionsViewerViewModel.InstructionsViewerState = InstructionsViewerViewModel.InstructionsViewerState.Idle
            viewModel.state.collect { state ->
                when(state) {
                    InstructionsViewerViewModel.InstructionsViewerState.Idle -> {
                        Assert.assertEquals(expectedState, state)
                        expectedState = InstructionsViewerViewModel.InstructionsViewerState.Loading
                    }
                    InstructionsViewerViewModel.InstructionsViewerState.Loading -> {
                        Assert.assertEquals(expectedState, state)
                        expectedState = InstructionsViewerViewModel.InstructionsViewerState.Error
                    }
                    is InstructionsViewerViewModel.InstructionsViewerState.Data -> {
                        Assert.assertEquals(expectedState, state)
                    }
                    InstructionsViewerViewModel.InstructionsViewerState.Error -> {
                        Assert.assertEquals(expectedState, state)
                        this.cancel()
                    }
                }
            }
        }
        viewModel.initViewModel("test")
        collector.join()
    }

    @Test
    fun `init viewmodel with usecase success configs proper states`() = runTest {
        val response = InstructionItem(Coordinates(5, 5), Coordinates(0,0), RoverDirection.NORTH, "", listOf())
        val firstStep = MovementStep(Coordinates(0,0), RoverDirection.NORTH, Coordinates(0,0), RoverDirection.NORTH, RoverMovement.MOVE, "")
        val report = InstructionResolution(response, Coordinates(0,0), RoverDirection.NORTH, "0 0 N", listOf(firstStep))
        useCase.useCaseResponse = Either.Right(response)
        viewModel = InstructionsViewerViewModel(savedStateHandle, resourcesProvider, useCase)
        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            var expectedState: InstructionsViewerViewModel.InstructionsViewerState = InstructionsViewerViewModel.InstructionsViewerState.Idle
            viewModel.state.collect { state ->
                when(state) {
                    InstructionsViewerViewModel.InstructionsViewerState.Idle -> {
                        Assert.assertEquals(expectedState, state)
                        expectedState = InstructionsViewerViewModel.InstructionsViewerState.Loading
                    }
                    InstructionsViewerViewModel.InstructionsViewerState.Loading -> {
                        Assert.assertEquals(expectedState, state)
                        expectedState = InstructionsViewerViewModel.InstructionsViewerState.Data(report)
                    }
                    is InstructionsViewerViewModel.InstructionsViewerState.Data -> {
                        Assert.assertEquals(expectedState, state)
                        this.cancel()
                    }
                    InstructionsViewerViewModel.InstructionsViewerState.Error -> {
                        Assert.assertEquals(expectedState, state)
                    }
                }
            }
        }
        viewModel.initViewModel("test")
        collector.join()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private class GetInstructionUseCaseFake(
        var useCaseResponse: Either<Failure, InstructionItem> = Either.Left(Failure.UnexpectedFailure(""))
    ) : GetInstructionUseCase {
        override suspend fun invoke(urlPath: String): Either<Failure, InstructionItem> {
            delay(10)
            return useCaseResponse
        }
    }

    private class ResourcesProviderFake(
        var stringResponse: String = ""
    ) : ResourcesProvider {
        override fun getString(resourceId: Int): String = stringResponse
    }
}