package dev.jorgeroldan.marsrover.ui.features.instructions

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.jorgeroldan.marsrover.domain.model.Instruction
import dev.jorgeroldan.marsrover.ui.util.PreviewMockData
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class SelectInstructionsViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val state = savedStateHandle.getStateFlow<SelectInstructionsState>(SELECT_INSTRUCTIONS_STATE, SelectInstructionsState.Idle)

    init {
        savedStateHandle[SELECT_INSTRUCTIONS_STATE] = SelectInstructionsState.Data(PreviewMockData.instructionList)
    }

    sealed interface SelectInstructionsState {
        @Parcelize
        data object Idle : SelectInstructionsState, Parcelable
        @Parcelize
        data object Loading : SelectInstructionsState, Parcelable
        @Parcelize
        data class Data(val items: @RawValue List<Instruction>): SelectInstructionsState, Parcelable
        @Parcelize
        data object Error : SelectInstructionsState, Parcelable
    }

    companion object {
        private const val SELECT_INSTRUCTIONS_STATE = "select_instructions_state"
    }
}