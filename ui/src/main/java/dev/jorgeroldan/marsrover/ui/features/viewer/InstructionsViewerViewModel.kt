package dev.jorgeroldan.marsrover.ui.features.viewer

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jorgeroldan.marsrover.domain.mapper.InstructionsMapper
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.domain.model.InstructionItem
import dev.jorgeroldan.marsrover.domain.model.InstructionResolution
import dev.jorgeroldan.marsrover.domain.usecase.GetInstructionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class InstructionsViewerViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getInstructionUseCase: GetInstructionUseCase,
    private val instructionUrlPath: String? = null,
    //private val instructionModel: InstructionItem? = null
) : ViewModel() {

    val state = savedStateHandle.getStateFlow<InstructionsViewerState>(INSTRUCTIONS_VIEWER_STATE, InstructionsViewerState.Idle)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            savedStateHandle[INSTRUCTIONS_VIEWER_STATE] = InstructionsViewerState.Loading
            if (instructionUrlPath != null) {
                getInstructionModel(instructionUrlPath)
            } /*else if (instructionModel != null) {
                getSolutionModel(instructionModel)
            } */else {
                savedStateHandle[INSTRUCTIONS_VIEWER_STATE] = InstructionsViewerState.Error
            }
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

        val result = InstructionsMapper.processRoverPosition(instructions)
        savedStateHandle[INSTRUCTIONS_VIEWER_STATE] = InstructionsViewerState.Data(result)
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