package dev.jorgeroldan.marsrover.ui.features.builder

import androidx.lifecycle.SavedStateHandle
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class InstructionsBuilderViewModelTest {

    private val savedStateHandle = SavedStateHandle()
    private lateinit var viewModel: InstructionsBuilderViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = InstructionsBuilderViewModel(savedStateHandle)
    }

    @Test
    fun `on init viewmodel sets default state`() {
        val current = viewModel.state.value
        Assert.assertTrue(current is InstructionsBuilderViewModel.InstructionsBuilderState.Data)
    }

    @Test
    fun `onViewEvent OnIncrementXFieldSize produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnIncrementXFieldSize)
        val result = viewModel.getCurrent()
        Assert.assertEquals(1, result.topRightCorner.x)
    }

    @Test
    fun `onViewEvent OnDecrementXFieldSize produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnDecrementXFieldSize)
        val result = viewModel.getCurrent()
        Assert.assertEquals(-1, result.topRightCorner.x)
    }

    @Test
    fun `onViewEvent OnIncrementYFieldSize produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnIncrementYFieldSize)
        val result = viewModel.getCurrent()
        Assert.assertEquals(1, result.topRightCorner.y)
    }

    @Test
    fun `onViewEvent OnDecrementYFieldSize produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnDecrementYFieldSize)
        val result = viewModel.getCurrent()
        Assert.assertEquals(-1, result.topRightCorner.y)
    }

    @Test
    fun `onViewEvent OnIncrementXRoverPosition produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnIncrementXRoverPosition)
        val result = viewModel.getCurrent()
        Assert.assertEquals(1, result.roverPosition.x)
    }

    @Test
    fun `onViewEvent OnDecrementXRoverPosition produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnDecrementXRoverPosition)
        val result = viewModel.getCurrent()
        Assert.assertEquals(-1, result.roverPosition.x)
    }

    @Test
    fun `onViewEvent OnIncrementYRoverPosition produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnIncrementYRoverPosition)
        val result = viewModel.getCurrent()
        Assert.assertEquals(1, result.roverPosition.y)
    }

    @Test
    fun `onViewEvent OnDecrementYRoverPosition produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnDecrementYRoverPosition)
        val result = viewModel.getCurrent()
        Assert.assertEquals(-1, result.roverPosition.y)
    }

    @Test
    fun `onViewEvent OnMovementAdded with SPIN_LEFT produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnMovementAdded(RoverMovement.SPIN_LEFT))
        val result = viewModel.getCurrent()
        Assert.assertEquals("L", result.encodedMovements)
    }

    @Test
    fun `onViewEvent OnMovementAdded with MOVE produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnMovementAdded(RoverMovement.MOVE))
        val result = viewModel.getCurrent()
        Assert.assertEquals("M", result.encodedMovements)
    }

    @Test
    fun `onViewEvent OnMovementAdded with SPIN_RIGHT produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnMovementAdded(RoverMovement.SPIN_RIGHT))
        val result = viewModel.getCurrent()
        Assert.assertEquals("R", result.encodedMovements)
    }

    @Test
    fun `onViewEvent OnMovementRemoved without movements produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnMovementRemoved)
        val result = viewModel.getCurrent()
        Assert.assertEquals("", result.encodedMovements)
    }

    @Test
    fun `onViewEvent OnMovementRemoved with movements produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnMovementAdded(RoverMovement.SPIN_RIGHT))
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnMovementAdded(RoverMovement.MOVE))
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnMovementAdded(RoverMovement.SPIN_RIGHT))
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnMovementRemoved)
        val result = viewModel.getCurrent()
        Assert.assertEquals("RM", result.encodedMovements)
    }

    @Test
    fun `onViewEvent OnOrientationChanged produces proper model`() {
        viewModel.onViewEvent(InstructionsBuilderViewModel.InstructionsBuilderEvents.OnOrientationChanged(RoverDirection.EAST))
        val result = viewModel.getCurrent()
        Assert.assertEquals(RoverDirection.EAST, result.roverDirection)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}