package dev.jorgeroldan.marsrover.ui.features.instructions

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import arrow.core.Either
import dev.jorgeroldan.marsrover.domain.model.Failure
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionsListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SelectInstructionsViewModelTest {

    private val savedStateHandle = SavedStateHandle()
    private val useCase = GetInstructionListUseCaseFake()
    private lateinit var viewModel: SelectInstructionsViewModel
    private val defaultDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(defaultDispatcher)
    }

    @Test
    fun `init viewmodel with usecase error configs proper states`() = runTest {
        useCase.useCaseResponse = Either.Left(Failure.UnexpectedFailure(""))
        viewModel = SelectInstructionsViewModel(savedStateHandle, useCase, defaultDispatcher)
        viewModel.state.test {
            delay(100)
            val item = expectMostRecentItem()
            Assert.assertEquals(SelectInstructionsViewModel.SelectInstructionsState.Error, item)
            cancel()
        }
    }

    @Test
    fun `init viewmodel with usecase success configs proper states`() = runTest {
        useCase.useCaseResponse = Either.Right(listOf())
        viewModel = SelectInstructionsViewModel(savedStateHandle, useCase, defaultDispatcher)
        viewModel.state.test {
            delay(100)
            val item = expectMostRecentItem()
            Assert.assertTrue(item is SelectInstructionsViewModel.SelectInstructionsState.Data)
            cancel()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private class GetInstructionListUseCaseFake(
        var useCaseResponse: Either<Failure, List<Instruction>> = Either.Left(Failure.UnexpectedFailure(""))
    ) : GetInstructionsListUseCase {
        override suspend fun invoke(): Either<Failure, List<Instruction>> = useCaseResponse
    }
}