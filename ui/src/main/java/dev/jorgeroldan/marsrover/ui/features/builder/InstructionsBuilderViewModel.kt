package dev.jorgeroldan.marsrover.ui.features.builder

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.jorgeroldan.marsrover.domain.model.Coordinates
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.RoverDirection
import dev.jorgeroldan.marsrover.domain.model.RoverMovement
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class InstructionsBuilderViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val state = savedStateHandle.getStateFlow<InstructionsBuilderState>(INSTRUCTIONS_BUILDER_STATE, InstructionsBuilderState.Idle)

    init {
        initViewmodel()
    }

    private fun initViewmodel() {
        val starterModel = buildStarterModel()
        savedStateHandle[INSTRUCTIONS_BUILDER_VALUES] = starterModel
        savedStateHandle[INSTRUCTIONS_BUILDER_STATE] = InstructionsBuilderState.Data(starterModel)
    }

    fun onViewEvent(event: InstructionsBuilderEvents): Unit = when (event) {
        InstructionsBuilderEvents.OnIncrementXFieldSize -> modifyXFieldSize(increment = true)
        InstructionsBuilderEvents.OnDecrementXFieldSize -> modifyXFieldSize(increment = false)
        InstructionsBuilderEvents.OnIncrementYFieldSize -> modifyYFieldSize(increment = true)
        InstructionsBuilderEvents.OnDecrementYFieldSize -> modifyYFieldSize(increment = false)
        InstructionsBuilderEvents.OnIncrementXRoverPosition -> modifyXRoverPosition(increment = true)
        InstructionsBuilderEvents.OnDecrementXRoverPosition -> modifyXRoverPosition(increment = false)
        InstructionsBuilderEvents.OnIncrementYRoverPosition -> modifyYRoverPosition(increment = true)
        InstructionsBuilderEvents.OnDecrementYRoverPosition -> modifyYRoverPosition(increment = false)
        is InstructionsBuilderEvents.OnMovementAdded -> onMovementAdded(event.movement)
        InstructionsBuilderEvents.OnMovementRemoved -> onMovementRemoved()
        is InstructionsBuilderEvents.OnOrientationChanged -> onOrientationChanged(event.orientation)
    }

    fun getCurrent(): InstructionItem {
        return savedStateHandle.get<InstructionItem>(INSTRUCTIONS_BUILDER_VALUES) ?: buildStarterModel()
    }

    private fun modifyXFieldSize(increment: Boolean) {
        produceChanges { item ->
            val newValue = if (increment) item.topRightCorner.x + 1 else item.topRightCorner.x - 1
            val fieldCoordinates = item.topRightCorner.copy(x = newValue)
            item.copy(topRightCorner = fieldCoordinates)
        }
    }

    private fun modifyYFieldSize(increment: Boolean) {
        produceChanges { item ->
            val newValue = if (increment) item.topRightCorner.y + 1 else item.topRightCorner.y - 1
            val fieldCoordinates = item.topRightCorner.copy(y = newValue)
            item.copy(topRightCorner = fieldCoordinates)
        }
    }

    private fun modifyXRoverPosition(increment: Boolean) {
        produceChanges { item ->
            val newValue = if (increment) item.roverPosition.x + 1 else item.roverPosition.x - 1
            val fieldCoordinates = item.roverPosition.copy(x = newValue)
            item.copy(roverPosition = fieldCoordinates)
        }
    }

    private fun modifyYRoverPosition(increment: Boolean) {
        produceChanges { item ->
            val newValue = if (increment) item.roverPosition.y + 1 else item.roverPosition.y - 1
            val fieldCoordinates = item.roverPosition.copy(y = newValue)
            item.copy(roverPosition = fieldCoordinates)
        }
    }

    private fun onMovementAdded(movement: RoverMovement) {
        produceChanges { item ->
            val updatedMovements = item.movements.toMutableList()
            updatedMovements.add(movement)
            val updatedEncoded = item.encodedMovements + movement.toString()
            item.copy(encodedMovements = updatedEncoded, movements = updatedMovements)
        }
    }

    private fun onMovementRemoved() {
        produceChanges { item ->
            val updatedMovements = item.movements.toMutableList()
            if (updatedMovements.isNotEmpty()) {
                updatedMovements.removeLast()
            }
            val updatedEncoded = item.encodedMovements.dropLast(1)
            item.copy(encodedMovements = updatedEncoded, movements = updatedMovements)
        }
    }

    private fun onOrientationChanged(orientation: RoverDirection) {
        produceChanges { item ->
            item.copy(roverDirection = orientation)
        }
    }

    private fun produceChanges(block: (InstructionItem) -> InstructionItem) {
        val model = savedStateHandle.get<InstructionItem>(INSTRUCTIONS_BUILDER_VALUES) ?: buildStarterModel()
        val updated = block(model)
        savedStateHandle[INSTRUCTIONS_BUILDER_VALUES] = updated
        savedStateHandle[INSTRUCTIONS_BUILDER_STATE] = InstructionsBuilderState.Data(updated)
    }

    private fun buildStarterModel() = InstructionItem(
        topRightCorner = Coordinates(0, 0),
        roverPosition = Coordinates(0, 0),
        roverDirection = RoverDirection.NORTH,
        encodedMovements = "",
        movements = listOf()
    )

    sealed interface InstructionsBuilderState {
        @Parcelize
        data object Idle : InstructionsBuilderState, Parcelable
        @Parcelize
        data class Data(val item: @RawValue InstructionItem): InstructionsBuilderState, Parcelable
    }

    sealed interface InstructionsBuilderEvents {
        data object OnIncrementXFieldSize : InstructionsBuilderEvents
        data object OnDecrementXFieldSize : InstructionsBuilderEvents
        data object OnIncrementYFieldSize : InstructionsBuilderEvents
        data object OnDecrementYFieldSize : InstructionsBuilderEvents
        data object OnIncrementXRoverPosition : InstructionsBuilderEvents
        data object OnDecrementXRoverPosition : InstructionsBuilderEvents
        data object OnIncrementYRoverPosition : InstructionsBuilderEvents
        data object OnDecrementYRoverPosition : InstructionsBuilderEvents
        data class OnMovementAdded(val movement: RoverMovement): InstructionsBuilderEvents
        data object OnMovementRemoved : InstructionsBuilderEvents
        data class OnOrientationChanged(val orientation: RoverDirection): InstructionsBuilderEvents
    }

    companion object {
        private const val INSTRUCTIONS_BUILDER_STATE = "instructions_builder_state"
        private const val INSTRUCTIONS_BUILDER_VALUES = "instructions_builder_values"
    }
}