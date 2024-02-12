package dev.jorgeroldan.marsrover.ui.features.viewer

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jorgeroldan.marsrover.domain.mapper.InstructionsMapper
import dev.jorgeroldan.marsrover.domain.model.FriendlyUserText
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionUseCase
import dev.jorgeroldan.marsrover.ui.R
import dev.jorgeroldan.marsrover.ui.util.ResourcesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class InstructionsViewerViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val resourcesProvider: ResourcesProvider,
    private val getInstructionUseCase: GetInstructionUseCase,
) : ViewModel() {

    val state = savedStateHandle.getStateFlow<InstructionsViewerState>(INSTRUCTIONS_VIEWER_STATE, InstructionsViewerState.Idle)

    fun initViewModel(
        instructionUrlPath: String? = null,
        instructionModel: InstructionItem? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        savedStateHandle[INSTRUCTIONS_VIEWER_STATE] = InstructionsViewerState.Loading
        if (instructionUrlPath != null) {
            getInstructionModel(instructionUrlPath)
        } else if (instructionModel != null) {
            getSolutionModel(instructionModel)
        } else {
            savedStateHandle[INSTRUCTIONS_VIEWER_STATE] = InstructionsViewerState.Error
        }
    }


    private suspend fun getInstructionModel(urlPath: String) {
        getInstructionUseCase
            .invoke(urlPath)
            .fold(
                ifLeft = { savedStateHandle[INSTRUCTIONS_VIEWER_STATE] = InstructionsViewerState.Error },
                ifRight = { getSolutionModel(it) }
            )
    }

    private suspend fun getSolutionModel(instructions: InstructionItem) {
        InstructionsMapper.processRoverPosition(
            instruction = instructions,
            userTexts = FriendlyUserText(
                startText = resourcesProvider.getString(R.string.instructions_viewer_events_start_event),
                moveText = resourcesProvider.getString(R.string.instructions_viewer_events_move_event),
                rotateText = resourcesProvider.getString(R.string.instructions_viewer_events_rotate_event),
            )
        ).fold(
            ifLeft = { savedStateHandle[INSTRUCTIONS_VIEWER_STATE] = InstructionsViewerState.Error },
            ifRight = { savedStateHandle[INSTRUCTIONS_VIEWER_STATE] = InstructionsViewerState.Data(it) }
        )
    }

    sealed interface InstructionsViewerState {
        @Parcelize
        data object Idle : InstructionsViewerState, Parcelable
        @Parcelize
        data object Loading : InstructionsViewerState, Parcelable
        @Parcelize
        data class Data(val result: @RawValue InstructionResolution): InstructionsViewerState, Parcelable
        @Parcelize
        data object Error : InstructionsViewerState, Parcelable
    }

    companion object {
        private const val INSTRUCTIONS_VIEWER_STATE = "instructions_viewer_state"
    }
}